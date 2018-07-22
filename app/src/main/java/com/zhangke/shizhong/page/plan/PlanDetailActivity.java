package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IEditPlanContract;
import com.zhangke.shizhong.contract.plan.IPlanDetailContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.presenter.plan.PlanDetailPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 计划详情界面
 * <p>
 * Created by ZhangKe on 2018/7/22.
 */
public class PlanDetailActivity extends BaseActivity implements IPlanDetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_start_date)
    EditText etStartDate;
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

    private long planId;
    private int planType;

    private IPlanDetailContract.Presenter presenter;

    public static void open(Context context, long planId, int planType) {
        Intent intent = new Intent(context, PlanDetailActivity.class);
        intent.putExtra(INTENT_ARG_01, planId);
        intent.putExtra(INTENT_ARG_02, planType);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_plan_detail;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar(toolbar, "详细内容", true);

        planId = getIntent().getLongExtra(INTENT_ARG_01, -1L);
        planType = getIntent().getIntExtra(INTENT_ARG_02, 0);

        presenter = new PlanDetailPresenterImpl(this, this);

        presenter.initDate(planId, planType);
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
        etName.setText(plan.getName());
        etStartDate.setText(plan.getStartDate());
        etFinishDate.setText(plan.getFinishDate());
        etTarget.setText(String.valueOf(plan.getTarget()));
        etCurrent.setText(String.valueOf(plan.getCurrent()));
        etUnit.setText(plan.getUnit());
    }

    @Override
    public void fillClockPlanData(ClockPlan plan) {
        etName.setText(plan.getName());
        etStartDate.setText(plan.getStartDate());
        etDescription.setText(plan.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plan_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                EditPlanActivity.open(this, planId, planType);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PlanChangedEvent event) {
        presenter.initDate(planId, planType);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
