package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IEditPlanContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.model.plan.EditPlanDataEntity;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.presenter.plan.EditPlanPresenterImpl;
import com.zhangke.shizhong.util.DateTimePickerHelper;
import com.zhangke.shizhong.util.DateUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改计划界面
 * <p>
 * Created by ZhangKe on 2018/7/19.
 */
public class EditPlanActivity extends BaseActivity implements IEditPlanContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.fl_plan_description)
    FrameLayout flPlanDescription;
    @BindView(R.id.et_finish_date)
    EditText etFinishDate;
    @BindView(R.id.et_target)
    EditText etTarget;
    @BindView(R.id.et_current)
    EditText etCurrent;
    @BindView(R.id.et_unit)
    EditText etUnit;
    @BindView(R.id.ll_plan_switch)
    LinearLayout llPlanSwitch;
    @BindView(R.id.et_period_type)
    EditText etPeriodType;
    @BindView(R.id.et_period_target)
    EditText etPeriodTarget;
    @BindView(R.id.ll_period)
    LinearLayout llPeriod;

    /**
     * 计划ID
     */
    private long planId;
    /**
     * 0-定量计划
     * 1-打卡计划
     */
    private int planType;

    private IEditPlanContract.Presenter presenter;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    /**
     * 短期计划类型
     * 0-天
     * 1-周
     * 2-月
     */
    private int periodType = 0;
    private RationPlan rationPlan;

    public static void open(Context context, long planId, int planType) {
        Intent intent = new Intent(context, EditPlanActivity.class);
        intent.putExtra(INTENT_ARG_01, planId);
        intent.putExtra(INTENT_ARG_02, planType);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_plan;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "修改计划", true);

        planId = getIntent().getLongExtra(INTENT_ARG_01, -1L);
        planType = getIntent().getIntExtra(INTENT_ARG_02, 1);

        presenter = new EditPlanPresenterImpl(this, this);

        presenter.initDate(planId, planType);
    }

    @OnClick(R.id.btn_edit)
    public void onEdit() {
        if (planType == 0 && !checkRationPlan()) {
            return;
        }
        if (planType == 1 && !checkClockPlan()) {
            return;
        }
        EditPlanDataEntity editData = new EditPlanDataEntity();
        editData.setName(etName.getText().toString());
        editData.setFinishDate(etFinishDate.getText().toString());
        editData.setDescription(etDescription.getText().toString());
        editData.setTarget(planType == 0 ? Double.valueOf(etTarget.getText().toString()) : 0);
        editData.setCurrent(planType == 0 ? Double.valueOf(etCurrent.getText().toString()) : 0);
        editData.setUnit(etUnit.getText().toString());
        boolean periodIsOpen = llPeriod.getVisibility() == View.VISIBLE;
        editData.setPeriodIsOpen(periodIsOpen);
        if (periodIsOpen) {
            editData.setPeriodTarget(Double.valueOf(etPeriodTarget.getText().toString()));
            switch(etPeriodType.getText().toString()){
                case "天":
                    editData.setPeriodPlanType(0);
                    break;
                case "周":
                    editData.setPeriodPlanType(1);
                    break;
                case "月":
                    editData.setPeriodPlanType(3);
                    break;
            }
        }
        presenter.updatePlan(editData);
    }

    private boolean checkRationPlan() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            showNoActionSnackbar("请输入计划名");
            return false;
        }
        if (TextUtils.isEmpty(etFinishDate.getText().toString())) {
            showNoActionSnackbar("请输入结束时间");
            return false;
        }
        if (TextUtils.isEmpty(etTarget.getText().toString())) {
            showNoActionSnackbar("请输入目标值");
            return false;
        }
        if (TextUtils.isEmpty(etCurrent.getText().toString())) {
            showNoActionSnackbar("请输入当前值");
            return false;
        }
        if (TextUtils.isEmpty(etUnit.getText().toString())) {
            showNoActionSnackbar("请输入单位");
            return false;
        }
        if (llPeriod.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(etPeriodType.getText().toString())) {
                showNoActionSnackbar("请选择短期计划类型");
                return false;
            }
            if (TextUtils.isEmpty(etPeriodTarget.getText().toString())) {
                showNoActionSnackbar("请输入短期计划目标");
                return false;
            }
        }
        return true;
    }

    private boolean checkClockPlan() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            showNoActionSnackbar("请输入计划名");
            return false;
        }
        return true;
    }

    @OnClick(R.id.et_finish_date)
    public void onFinishDateClick() {
        DateTimePickerHelper.showDateDialog(this,
                "yyyy-MM-dd",
                TextUtils.isEmpty(etFinishDate.getText().toString()) ? "" : etFinishDate.getText().toString(),
                etFinishDate::setText);
    }

    @OnClick(R.id.et_period_type)
    public void onPeriodTypeClick(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add(0, 0, 0, "天");
        popupMenu.getMenu().add(0, 1, 0, "周");
        popupMenu.getMenu().add(0, 2, 0, "月");
        popupMenu.setOnMenuItemClickListener(item -> {
            etPeriodType.setText(item.getTitle());
            this.periodType = item.getItemId();
            updatePeriodTarget();
            return true;
        });
        popupMenu.show();
    }

    @OnClick(R.id.img_delete_period)
    public void onDeletePeriodPlanClick() {
        new AlertDialog.Builder(this)
                .setTitle("真的要删除短期计划？")
                .setNegativeButton("不", null)
                .setPositiveButton("对", (DialogInterface dialog, int which) -> {
                    showRoundProgressDialog();
                    presenter.deletePeriod();
                })
                .create()
                .show();
    }

    private void updatePeriodTarget() {
        if (rationPlan == null) return;
        double target = !TextUtils.isEmpty(etTarget.getText().toString()) ?
                Double.valueOf(etTarget.getText().toString()) :
                rationPlan.getTarget();
        double current = !TextUtils.isEmpty(etCurrent.getText().toString()) ?
                Double.valueOf(etCurrent.getText().toString()) :
                rationPlan.getCurrent();
        String startDate = rationPlan != null ?
                rationPlan.getStartDate() :
                "";
        String finishDate = !TextUtils.isEmpty(etFinishDate.getText().toString()) ?
                etFinishDate.getText().toString() :
                rationPlan.getFinishDate();
        if (target != 0
                && !TextUtils.isEmpty(startDate)
                && !TextUtils.isEmpty(finishDate)) {
            switch (periodType) {
                case 0: {
                    //天
                    double value = (target - current) / (DateUtils.getDaySpace("yyyy-MM-dd", startDate, finishDate) + 1);
                    etPeriodTarget.setText(decimalFormat.format(value));
                    break;
                }
                case 1: {
                    //周
                    double value = (target - current) / (DateUtils.getWeekSpace("yyyy-MM-dd", startDate, finishDate) + 1);
                    etPeriodTarget.setText(decimalFormat.format(value));
                    break;
                }
                case 2: {
                    //月
                    double value = (target - current) / (DateUtils.getMonthSpace("yyyy-MM-dd", startDate, finishDate) + 1);
                    etPeriodTarget.setText(decimalFormat.format(value));
                    break;
                }
            }
        }
    }

    @Override
    public void showRationPlan() {
        flPlanDescription.setVisibility(View.GONE);
        llPlanSwitch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showClockPlan() {
        flPlanDescription.setVisibility(View.VISIBLE);
        llPlanSwitch.setVisibility(View.GONE);
    }

    @Override
    public void fillRationPlanData(RationPlan plan) {
        this.rationPlan = plan;
        etName.setText(plan.getName());
        etFinishDate.setText(plan.getFinishDate());
        etTarget.setText(String.valueOf(plan.getTarget()));
        etCurrent.setText(String.valueOf(plan.getCurrent()));
        etUnit.setText(plan.getUnit());
        if (plan.getPeriodIsOpen()) {
            llPeriod.setVisibility(View.VISIBLE);
            int periodType = plan.getPeriodPlanType();
            switch (periodType) {
                case 0:
                    etPeriodType.setText("日");
                    break;
                case 1:
                    etPeriodType.setText("周");
                    break;
                case 2:
                    etPeriodType.setText("月");
                    break;
            }
            etPeriodTarget.setText(String.valueOf(plan.getPeriodPlanTarget()));
        } else {
            llPeriod.setVisibility(View.GONE);
        }
        closeRoundProgressDialog();
    }

    @Override
    public void fillClockPlanData(ClockPlan plan) {
        etName.setText(plan.getName());
        etDescription.setText(plan.getDescription());
        closeRoundProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("注意了");
                builder.setMessage("真的要删除这个计划吗？？？");
                builder.setNegativeButton("不不不", null);
                builder.setPositiveButton("删吧", (DialogInterface dialog, int which) -> presenter.deletePlan());
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
