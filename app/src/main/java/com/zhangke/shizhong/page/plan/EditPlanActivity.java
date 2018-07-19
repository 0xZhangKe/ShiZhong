package com.zhangke.shizhong.page.plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IEditPlanContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.model.plan.EditPlanDataEntity;
import com.zhangke.shizhong.page.base.BaseActivity;

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
    @BindView(R.id.til_description)
    TextInputLayout tilDescription;
    @BindView(R.id.et_finish_date)
    EditText etFinishDate;
    @BindView(R.id.et_target_value)
    EditText etTargetValue;
    @BindView(R.id.et_cur_value)
    EditText etCurValue;
    @BindView(R.id.et_unit)
    EditText etUnit;
    @BindView(R.id.ll_plan_switch)
    LinearLayout llPlanSwitch;

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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_plan;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        planId = getIntent().getLongExtra(INTENT_ARG_01, -1L);
        planType = getIntent().getIntExtra(INTENT_ARG_02, 1);

        presenter.initDate(planId, planType);
    }

    @OnClick(R.id.btn_edit)
    public void onEdit(){
        if(planType == 0 && !checkRationPlan()){
            return;
        }
        if(planType == 1 && !checkClockPlan()){
            return;
        }
        EditPlanDataEntity editData = new EditPlanDataEntity();
        editData.setName(etName.getText().toString());
        editData.setFinishDate(etFinishDate.getText().toString());
        editData.setDescription(etDescription.getText().toString());
        editData.setTarget(Double.valueOf(etTargetValue.getText().toString()));
        editData.setCurrent(Double.valueOf(etCurValue.getText().toString()));
        editData.setUnit(etUnit.getText().toString());
        presenter.updatePlan(editData);
    }

    private boolean checkRationPlan(){
        if(TextUtils.isEmpty(etName.getText().toString())){
            showNoActionSnackbar("请输入计划名");
            return false;
        }
        if(TextUtils.isEmpty(etFinishDate.getText().toString())){
            showNoActionSnackbar("请输入结束时间");
            return false;
        }
        if(TextUtils.isEmpty(etTargetValue.getText().toString())){
            showNoActionSnackbar("请输入目标值");
            return false;
        }
        if(TextUtils.isEmpty(etCurValue.getText().toString())){
            showNoActionSnackbar("请输入当前值");
            return false;
        }
        if(TextUtils.isEmpty(etUnit.getText().toString())){
            showNoActionSnackbar("请输入单位");
            return false;
        }
        return true;
    }

    private boolean checkClockPlan(){
        if(TextUtils.isEmpty(etName.getText().toString())){
            showNoActionSnackbar("请输入计划名");
            return false;
        }
        return true;
    }

    @Override
    public void showRationPlan() {
        tilDescription.setVisibility(View.GONE);
        llPlanSwitch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showClockPlan() {
        tilDescription.setVisibility(View.VISIBLE);
        llPlanSwitch.setVisibility(View.GONE);
    }

    @Override
    public void fillRationPlanData(RationPlan plan) {
        etName.setText(plan.getName());
        etFinishDate.setText(plan.getFinishDate());
        etTargetValue.setText(String.valueOf(plan.getTarget()));
        etCurValue.setText(String.valueOf(plan.getCurrent()));
        etUnit.setText(plan.getUnit());
    }

    @Override
    public void fillClockPlanData(ClockPlan plan) {
        etName.setText(plan.getName());
        etDescription.setText(plan.getDescription());
    }
}
