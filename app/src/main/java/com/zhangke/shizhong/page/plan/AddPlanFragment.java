package com.zhangke.shizhong.page.plan;

import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.event.PlanSelectedEvent;
import com.zhangke.shizhong.page.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 添加计划页面
 * <p>
 * Created by ZhangKe on 2018/7/10.
 */
public class AddPlanFragment extends BaseFragment {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_start_date)
    EditText etStartDate;
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
    Unbinder unbinder;

    /**
     * -1-未选择
     * 0-定量计划
     * 1-打卡计划
     */
    private int planType = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_plan;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.btn_add)
    public void addPlanBtnClick() {
        if (planType == -1) {
            showNoActionSnackbar("请先选择计划类型");
            return;
        }
        String name = etName.getText().toString();
        String startDate = etStartDate.getText().toString();

        if (planType == 0 && assertRationInput()) {
            String finishDate = etFinishDate.getText().toString();
            String targetValue = etTargetValue.getText().toString();
            String curValue = etCurValue.getText().toString();
            String unit = etUnit.getText().toString();
            final RationPlan plan = new RationPlan();
            plan.setName(name);
            plan.setStartDate(startDate);
            plan.setFinishDate(finishDate);
            plan.setTarget(Double.valueOf(targetValue));
            plan.setCurrent(Double.valueOf(curValue));
            plan.setUnit(unit);
            Observable.create(e -> {
                DBManager.getInstance().getRationPlanDao().insert(plan);
                e.onNext(1);
                e.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        EventBus.getDefault().post(new PlanChangedEvent());
                        mActivity.finish();
                    });
        }
        if (planType == 1 && !assertClockInput()) {
            final ClockPlan plan = new ClockPlan();
            plan.setName(name);
            plan.setStartDate(startDate);
            Observable.create(e -> {
                DBManager.getInstance().getClockPlanDao().insert(plan);
                e.onNext(1);
                e.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        EventBus.getDefault().post(new PlanChangedEvent());
                        mActivity.finish();
                    });
        }
    }

    private boolean assertRationInput() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            showNoActionSnackbar("请输入计划名");
            return false;
        }
        if (TextUtils.isEmpty(etStartDate.getText().toString())) {
            showNoActionSnackbar("请输入开始日期");
            return false;
        }
        if (TextUtils.isEmpty(etFinishDate.getText().toString())) {
            showNoActionSnackbar("请输入结束日期");
            return false;
        }
        if (TextUtils.isEmpty(etTargetValue.getText().toString())) {
            showNoActionSnackbar("请输入目标值");
            return false;
        }
        if (TextUtils.isEmpty(etCurValue.getText().toString())) {
            showNoActionSnackbar("请输入当前值");
            return false;
        }
        if (TextUtils.isEmpty(etUnit.getText().toString())) {
            showNoActionSnackbar("请输入单位");
            return false;
        }
        return true;
    }

    private boolean assertClockInput() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            showNoActionSnackbar("请输入计划名");
            return false;
        }
        if (TextUtils.isEmpty(etStartDate.getText().toString())) {
            showNoActionSnackbar("请输入开始日期");
            return false;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PlanSelectedEvent event) {
        planType = event.getPlanType();
        if (planType == 0) {
            llPlanSwitch.setVisibility(View.VISIBLE);
        } else {
            llPlanSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
