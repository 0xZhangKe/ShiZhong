package com.zhangke.shizhong.page.plan;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IShowPlanContract;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.presenter.plan.ShowPlanPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示计划界面
 *
 * Created by ZhangKe on 2018/4/15.
 */

public class ShowPlanFragment extends BaseFragment implements IShowPlanContract.View{

    private IShowPlanContract.Presenter showPlanPresenter;
    private List<Plan> planList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        showPlanPresenter = new ShowPlanPresenterImpl(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        showPlanPresenter.getPlanData();
    }

    @Override
    public void notifyPlanDataChanged(List<Plan> list) {
        planList.clear();
        planList.addAll(list);
        //todo notify adapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ThemeChangedEvent event) {
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
