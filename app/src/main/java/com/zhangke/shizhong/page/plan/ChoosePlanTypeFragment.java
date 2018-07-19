package com.zhangke.shizhong.page.plan;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.event.PlanSelectedEvent;
import com.zhangke.shizhong.page.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择计划类型页面
 * <p>
 * Created by ZhangKe on 2018/7/10.
 */
public class ChoosePlanTypeFragment extends BaseFragment {

    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_choose_plan_type;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, rootView);

    }

    @OnClick(R.id.btn_ration_plan)
    public void openRationPlan(View view) {
        EventBus.getDefault().post(new PlanSelectedEvent(0));
    }

    @OnClick(R.id.btn_clock_plan)
    public void openClockPlan(View view) {
        EventBus.getDefault().post(new PlanSelectedEvent(1));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
