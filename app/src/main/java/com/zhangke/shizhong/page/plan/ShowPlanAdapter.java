package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.db.PlanDao;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.presenter.plan.PlanHelper;
import com.zhangke.shizhong.util.DateUtils;
import com.zhangke.shizhong.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 计划列表的适配器
 * Created by ZhangKe on 2018/5/17.
 */
public class ShowPlanAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.ViewHolder, ShowPlanEntity> {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private PlanDao planDao;

    public ShowPlanAdapter(Context context, List<ShowPlanEntity> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == 0 ?
                new ShowPlanViewHolder(inflater.inflate(R.layout.adapter_show_plan, parent, false)) :
                new AddPlanViewHolder(inflater.inflate(R.layout.adapter_add_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        if (holder instanceof ShowPlanViewHolder) {
            ShowPlanViewHolder showPlanViewHolder = (ShowPlanViewHolder) holder;
            final Plan plan = listData.get(position).getPlan();
            showPlanViewHolder.tvPlanName.setText(plan.getName());
            showPlanViewHolder.tvPlanDescription.setText(plan.getDescription());
            showPlanViewHolder.tvStartDate.setText(plan.getStartDate());
            showPlanViewHolder.tvFinishDate.setText(plan.getFinishDate());
            showPlanViewHolder.tvTarget.setText(String.format("%s %s", plan.getTarget(), plan.getUnit()));
            showPlanViewHolder.tvCurrent.setText(String.format("%s %s", plan.getCurrent(), plan.getUnit()));
            if (plan.getPeriodIsOpen()) {
                showPlanViewHolder.llOpenPeriodPlan.setVisibility(View.GONE);
                showPlanViewHolder.llShowPeriodPlan.setVisibility(View.VISIBLE);
                showPlanViewHolder.tvPeriodPlanType.setText(plan.getPeriodPlanType() == 0 ? "按天" : plan.getPeriodPlanType() == 1 ? "按周" : "按月");
                showPlanViewHolder.tvPeriodPlanTarget.setText(String.format("%s %s", plan.getPeriodPlanTarget(), plan.getUnit()));
                setupPeriodCurValue(showPlanViewHolder.tvPeriodPlanCurValue, plan);
            } else {
                showPlanViewHolder.llOpenPeriodPlan.setVisibility(View.VISIBLE);
                showPlanViewHolder.llShowPeriodPlan.setVisibility(View.GONE);
                showPlanViewHolder.llOpenPeriodPlan.setOnClickListener(v -> showAddPeriodPlanDialog(plan));
            }
        }
    }

    private void setupPeriodCurValue(TextView tv, Plan plan) {
        double currentValue = 0.0;
        List<ClockRecord> records = plan.getClockRecords();
        if (records != null && !records.isEmpty()) {
            for (ClockRecord record : records) {
                if (PlanHelper.isCurPeriod(plan.getPeriodPlanType(), record)) {
                    currentValue += record.getValue();
                }
            }
        }
        tv.setText(decimalFormat.format(currentValue));
    }

    private void showAddPeriodPlanDialog(Plan plan) {
        final View rootView = inflater.inflate(R.layout.dialog_add_period_plan, null);
        final TextView tvPeriodType = rootView.findViewById(R.id.tv_period_type);
        final EditText etPeriodTarget = rootView.findViewById(R.id.et_period_target);
        final TextView tvUnit = rootView.findViewById(R.id.tv_unit);
        tvUnit.setText(plan.getUnit());
        tvPeriodType.setOnClickListener(v -> showPeriodTypePopup(tvPeriodType));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("设置短期计划");
        builder.setView(rootView);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (DialogInterface dialog, int which) -> {
            String periodType = tvPeriodType.getText().toString();
            String periodTarget = etPeriodTarget.getText().toString();
            if (TextUtils.isEmpty(periodType)) {
                UiUtils.showToast(context, "请选择短期计划类型");
                return;
            }
            if (TextUtils.isEmpty(periodTarget)) {
                UiUtils.showToast(context, "请输入目标值");
                return;
            }
            int type = 0;
            switch (periodType) {
                case "天":
                    type = 0;
                    break;
                case "周":
                    type = 1;
                    break;
                case "月":
                    type = 2;
                    break;
            }
            addPeriodToPlan(plan, type, Double.valueOf(periodTarget));
        });
        builder.create().show();
    }

    private void showPeriodTypePopup(final TextView tv) {
        PopupMenu popupMenu = new PopupMenu(context, tv);
        popupMenu.getMenu().add(0, 0, 0, "天");
        popupMenu.getMenu().add(0, 1, 0, "周");
        popupMenu.getMenu().add(0, 2, 0, "月");
        popupMenu.setOnMenuItemClickListener(item -> {
            tv.setText(item.getTitle());
            return true;
        });
        popupMenu.show();
    }

    private void addPeriodToPlan(Plan plan, int periodType, double target) {
        if (planDao == null) {
            planDao = DBManager.getInstance().getPlanDao();
        }
        plan.setPeriodIsOpen(true);
        plan.setPeriodPlanType(periodType);
        plan.setPeriodPlanTarget(target);
        planDao.insertOrReplace(plan);
        EventBus.getDefault().post(new PlanChangedEvent());
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getType();
    }

    class ShowPlanViewHolder extends ViewHolder {

        @BindView(R.id.tv_plan_name)
        TextView tvPlanName;
        @BindView(R.id.tv_plan_description)
        TextView tvPlanDescription;
        @BindView(R.id.tv_start_date)
        TextView tvStartDate;
        @BindView(R.id.tv_finish_date)
        TextView tvFinishDate;
        @BindView(R.id.tv_target)
        TextView tvTarget;
        @BindView(R.id.tv_current)
        TextView tvCurrent;
        @BindView(R.id.ll_open_period_plan)
        LinearLayout llOpenPeriodPlan;
        @BindView(R.id.ll_show_period_plan)
        LinearLayout llShowPeriodPlan;
        @BindView(R.id.tv_period_plan_type)
        TextView tvPeriodPlanType;
        @BindView(R.id.tv_period_plan_target)
        TextView tvPeriodPlanTarget;
        @BindView(R.id.tv_period_plan_cur_value)
        TextView tvPeriodPlanCurValue;

        ShowPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AddPlanViewHolder extends ViewHolder {
        AddPlanViewHolder(View itemView) {
            super(itemView);
        }
    }
}
