package com.zhangke.shizhong.page.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IShowPlanContract;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.presenter.plan.ShowPlanPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 显示计划界面
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class ShowPlanFragment extends BaseFragment implements IShowPlanContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private IShowPlanContract.Presenter showPlanPresenter;
    private List<ShowPlanEntity> planList = new ArrayList<>();
    private ShowPlanAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        showPlanPresenter = new ShowPlanPresenterImpl(this);

        adapter = new ShowPlanAdapter(mActivity, planList);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ShowPlanEntity plan = planList.get(position);
            if (plan.getType() == 2) {
                Intent intent = new Intent(mActivity, AddPlanActivity.class);
                startActivity(intent);
            }
        });
        showPlanPresenter.getPlanData();
    }

    @Override
    public void notifyPlanDataChanged(List<ShowPlanEntity> list) {
        planList.clear();
        planList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ThemeChangedEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PlanChangedEvent event) {
        showPlanPresenter.getPlanData();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
