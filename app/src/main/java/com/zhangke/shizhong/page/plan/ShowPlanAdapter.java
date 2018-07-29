package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.db.ClockRecordDao;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.DaoSession;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationPlanDao;
import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.db.RationRecordDao;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.DateUtils;
import com.zhangke.shizhong.util.UiUtils;
import com.zhangke.shizhong.widget.CountDownTextView;
import com.zhangke.shizhong.widget.CustomAutoCompleteTextView;
import com.zhangke.shizhong.widget.NumberProgressBar;

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

    private static final String TAG = "ShowPlanAdapter";

    private RationPlanDao planDao;
    private RationRecordDao rationClockRecordDao;
    private ClockRecordDao clockRecordDao;

    public ShowPlanAdapter(Context context, List<ShowPlanEntity> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == 0 ?
                new RationPlanViewHolder(inflater.inflate(R.layout.adapter_show_ration_plan, parent, false)) :
                viewType == 1 ?
                        new ClockPlanViewHolder(inflater.inflate(R.layout.adapter_show_clock_plan, parent, false)) :
                        new AddPlanViewHolder(inflater.inflate(R.layout.adapter_add_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        final ShowPlanEntity plan = listData.get(position);
        if (holder instanceof RationPlanViewHolder) {
            RationPlanViewHolder showPlanViewHolder = (RationPlanViewHolder) holder;
            showPlanViewHolder.tvPlanName.setText(plan.getPlanName());
            showPlanViewHolder.tvTargetValue.setText(String.valueOf(plan.getRationPlan().getTarget()));
            showPlanViewHolder.tvTitleUnit.setText(plan.getRationPlan().getUnit());
            showPlanViewHolder.tvPlanInfo.setText(plan.getPlanInfo());
            showPlanViewHolder.tvCountDown.setTargetDate(plan.getRationPlan().getFinishDate(), "yyyy-MM-dd");
            showPlanViewHolder.progressPlan.setProgress(plan.getProgress());
            showPlanViewHolder.tvSurplus.setText(plan.getSurplus());
            showPlanViewHolder.tvClock.setOnClickListener(v -> showRationClockDialog(plan));
            if (plan.isPeriodIsOpen()) {
                showPlanViewHolder.tvAddShortPlanTip.setVisibility(View.GONE);
                showPlanViewHolder.llShortPlanView.setVisibility(View.VISIBLE);
                showPlanViewHolder.imgAddShortPlan.setVisibility(View.GONE);
                showPlanViewHolder.tvShortPlanTitle.setText(plan.getShortPlanTitle());
                showPlanViewHolder.tvShortPlanTarget.setText(plan.getShortPlanTarget());
                showPlanViewHolder.tvShortPlanSurplus.setText(plan.getShortPlanSurplus());
            } else {
                showPlanViewHolder.tvAddShortPlanTip.setVisibility(View.VISIBLE);
                showPlanViewHolder.llShortPlanView.setVisibility(View.GONE);
                showPlanViewHolder.imgAddShortPlan.setVisibility(View.VISIBLE);
                showPlanViewHolder.imgAddShortPlan.setOnClickListener(v -> showAddPeriodPlanDialog(plan.getRationPlan()));
            }
            showPlanViewHolder.imgEdit.setOnClickListener(v -> EditPlanActivity.open(context, plan.getRationPlan().getId(), plan.getType()));
            showPlanViewHolder.tvDetail.setOnClickListener(v -> RationPlanDetailActivity.open(context, plan.getRationPlan().getId()));
        } else if (holder instanceof ClockPlanViewHolder) {
            ClockPlanViewHolder clockPlanViewHolder = (ClockPlanViewHolder) holder;
            clockPlanViewHolder.tvPlanName.setText(plan.getPlanName());
            if (TextUtils.isEmpty(plan.getPlanInfo())) {
                clockPlanViewHolder.tvPlanDescription.setVisibility(View.GONE);
            } else {
                clockPlanViewHolder.tvPlanDescription.setVisibility(View.VISIBLE);
                clockPlanViewHolder.tvPlanDescription.setText(plan.getPlanInfo());
            }
            List<ClockRecord> records = plan.getClockPlan().getClockRecords();
            clockPlanViewHolder.tvClockCount.setText(String.valueOf(records == null ? 0 : records.size()));
            clockPlanViewHolder.tvClock.setOnClickListener(v -> showClockDialog(plan));
            clockPlanViewHolder.imgEdit.setOnClickListener(v -> EditPlanActivity.open(context, plan.getClockPlan().getId(), plan.getType()));
            clockPlanViewHolder.tvDetail.setOnClickListener(v -> ClockPlanDetailActivity.open(context, plan.getClockPlan().getId()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getType();
    }

    private void showClockDialog(ShowPlanEntity clockPlan) {
        final View rootView = inflater.inflate(R.layout.dialog_clock, null);
        final CustomAutoCompleteTextView etClockName = rootView.findViewById(R.id.et_clock_comment);
        if (clockPlan.getSuggestionInput() != null && !clockPlan.getSuggestionInput().isEmpty()) {
            ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, clockPlan.getSuggestionInput());
            etClockName.setAdapter(adapter);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("打卡");
        builder.setView(rootView);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (DialogInterface dialog, int which) -> {
            String comment = etClockName.getText().toString();
            requestClock(clockPlan.getClockPlan(), comment);
        });
        builder.create().show();
    }

    private void requestClock(ClockPlan clockPlan, String comment) {
        ClockRecord clockRecord = new ClockRecord();
        clockRecord.setParentPlanId(clockPlan.getId());
        clockRecord.setDate(DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        if (!TextUtils.isEmpty(comment) && !TextUtils.equals(comment, "null")) {
            clockRecord.setDescription(comment);
        }
        if (clockRecordDao == null) {
            clockRecordDao = DBManager.getInstance().getClockRecordDao();
        }
        clockRecordDao.insert(clockRecord);
        DBManager.getInstance().clear();
        EventBus.getDefault().post(new PlanChangedEvent());
    }

    /**
     * 显示计量打卡对话框
     */
    private void showRationClockDialog(ShowPlanEntity plan) {
        final View rootView = inflater.inflate(R.layout.dialog_ration_clock, null);
        final CustomAutoCompleteTextView etClockName = rootView.findViewById(R.id.et_clock_name);
        final EditText etClockValue = rootView.findViewById(R.id.et_clock_value);
        final TextView tvUnit = rootView.findViewById(R.id.tv_unit);
        if (plan.getSuggestionInput() != null && !plan.getSuggestionInput().isEmpty()) {
            ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, plan.getSuggestionInput());
            etClockName.setAdapter(adapter);
        }
        etClockValue.requestFocus();
        tvUnit.setText(plan.getRationPlan().getUnit());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("打卡");
        builder.setView(rootView);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (DialogInterface dialog, int which) -> {
            String clockName = etClockName.getText().toString();
            String clockValue = etClockValue.getText().toString();
            if (TextUtils.isEmpty(clockName)) {
                clockName = "其它";
            }
            if (TextUtils.isEmpty(clockValue)) {
                UiUtils.showToast(context, "请输入本次完成值");
                return;
            }
            rationClock(plan.getRationPlan(), clockName, Double.valueOf(clockValue));
        });
        builder.create().show();
        etClockValue.post(() -> showInputKeyBord(etClockValue));
    }

    /**
     * 打卡
     */
    private void rationClock(RationPlan plan, String clockName, double value) {
        if (planDao == null) {
            planDao = DBManager.getInstance().getRationPlanDao();
        }
        if (rationClockRecordDao == null) {
            rationClockRecordDao = DBManager.getInstance().getRationRecordDao();
        }
        plan.setCurrent(plan.getCurrent() + value);
        planDao.insertOrReplace(plan);
        RationRecord clockRecord = new RationRecord();
        clockRecord.setDate(DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        clockRecord.setName(clockName);
        clockRecord.setParentPlanId(plan.getId());
        clockRecord.setValue(value);
        rationClockRecordDao.insertOrReplace(clockRecord);
        DBManager.getInstance().clear();
        EventBus.getDefault().post(new PlanChangedEvent());
    }

    /**
     * 显示添加短期计划对话框
     */
    private void showAddPeriodPlanDialog(RationPlan plan) {
        final View rootView = inflater.inflate(R.layout.dialog_add_period_plan, null);
        final TextView tvPeriodType = rootView.findViewById(R.id.tv_period_type);
        final EditText etPeriodTarget = rootView.findViewById(R.id.et_period_target);
        final TextView tvUnit = rootView.findViewById(R.id.tv_unit);
        tvUnit.setText(plan.getUnit());
        tvPeriodType.setOnClickListener(v -> showPeriodTypePopup(plan, tvPeriodType, etPeriodTarget));

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

    private void showInputKeyBord(EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private void showPeriodTypePopup(final RationPlan plan, final TextView tv, final EditText editText) {
        PopupMenu popupMenu = new PopupMenu(context, tv);
        popupMenu.getMenu().add(0, 0, 0, "天");
        popupMenu.getMenu().add(0, 1, 0, "周");
        popupMenu.getMenu().add(0, 2, 0, "月");
        popupMenu.setOnMenuItemClickListener(item -> {
            tv.setText(item.getTitle());
            switch (item.getItemId()) {
                case 0: {
                    //天
                    double value = (plan.getTarget() - plan.getCurrent()) / (DateUtils.getDaySpace("yyyy-MM-dd", plan.getStartDate(), plan.getFinishDate()) + 1);
                    editText.setText(decimalFormat.format(value));
                    break;
                }
                case 1: {
                    //周
                    double value = (plan.getTarget() - plan.getCurrent()) / (DateUtils.getWeekSpace("yyyy-MM-dd", plan.getStartDate(), plan.getFinishDate()) + 1);
                    editText.setText(decimalFormat.format(value));
                    break;
                }
                case 2: {
                    //月
                    double value = (plan.getTarget() - plan.getCurrent()) / (DateUtils.getMonthSpace("yyyy-MM-dd", plan.getStartDate(), plan.getFinishDate()) + 1);
                    editText.setText(decimalFormat.format(value));
                    break;
                }
            }
            return true;
        });
        popupMenu.show();
    }

    private void addPeriodToPlan(RationPlan plan, int periodType, double target) {
        if (planDao == null) {
            planDao = DBManager.getInstance().getRationPlanDao();
        }
        plan.setPeriodIsOpen(true);
        plan.setPeriodPlanType(periodType);
        plan.setPeriodPlanTarget(target);
        planDao.insertOrReplace(plan);
        EventBus.getDefault().post(new PlanChangedEvent());
    }

    class RationPlanViewHolder extends ViewHolder {

        @BindView(R.id.tv_plan_name)
        TextView tvPlanName;
        @BindView(R.id.tv_target_value)
        TextView tvTargetValue;
        @BindView(R.id.tv_title_unit)
        TextView tvTitleUnit;
        @BindView(R.id.tv_plan_info)
        TextView tvPlanInfo;
        @BindView(R.id.tv_count_down)
        CountDownTextView tvCountDown;
        @BindView(R.id.progress_plan)
        NumberProgressBar progressPlan;
        @BindView(R.id.tv_surplus)
        TextView tvSurplus;
        @BindView(R.id.ll_short_plan_view)
        LinearLayout llShortPlanView;
        @BindView(R.id.tv_short_plan_title)
        TextView tvShortPlanTitle;
        @BindView(R.id.tv_short_plan_target)
        TextView tvShortPlanTarget;
        @BindView(R.id.tv_short_plan_surplus)
        TextView tvShortPlanSurplus;
        @BindView(R.id.tv_add_short_plan_tip)
        TextView tvAddShortPlanTip;
        @BindView(R.id.tv_clock)
        TextView tvClock;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.img_add_short_plan)
        ImageView imgAddShortPlan;
        @BindView(R.id.img_edit)
        ImageView imgEdit;

        RationPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ClockPlanViewHolder extends ViewHolder {

        @BindView(R.id.tv_plan_name)
        TextView tvPlanName;
        @BindView(R.id.tv_plan_description)
        TextView tvPlanDescription;
        @BindView(R.id.tv_clock_count)
        TextView tvClockCount;
        @BindView(R.id.tv_clock)
        TextView tvClock;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.img_edit)
        ImageView imgEdit;

        ClockPlanViewHolder(View itemView) {
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
