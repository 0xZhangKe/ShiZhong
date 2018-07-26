package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IRationPlanDetailContract;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.presenter.plan.PlanHelper;
import com.zhangke.shizhong.presenter.plan.RationPlanDetailPresenterImpl;
import com.zhangke.shizhong.widget.SemicircleProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 定量计划详情页
 * <p>
 * Created by ZhangKe on 2018/7/26.
 */
public class RationPlanDetailActivity extends BaseActivity implements IRationPlanDetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.round_progress)
    SemicircleProgressView roundProgress;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_target_value)
    TextView tvTargetValue;

    private long planId;
    private List<RationRecord> listData = new ArrayList<>();
    private RationPlanDetailAdapter adapter;

    private IRationPlanDetailContract.Presenter presenter;

    public static void open(Context context, long planId) {
        Intent intent = new Intent(context, RationPlanDetailActivity.class);
        intent.putExtra(INTENT_ARG_01, planId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ration_plan_detail;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar(toolbar, "详细内容", true);

        planId = getIntent().getLongExtra(INTENT_ARG_01, -1L);

        adapter = new RationPlanDetailAdapter(this, listData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter = new RationPlanDetailPresenterImpl(this, planId);
        presenter.update();
    }

    @Override
    public void fillRationPlanData(RationPlan rationPlan) {
        roundProgress.setSesameValues(PlanHelper.getProgress(rationPlan), 100);
        tvTargetValue.setText(String.valueOf(rationPlan.getTarget()));
    }

    @Override
    public void notifyDataChanged(List<RationRecord> list) {
        listData.clear();
        listData.addAll(list);
        adapter.notifyDataSetChanged();
        if(listData.isEmpty()){
            recyclerView.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PlanChangedEvent event) {
        presenter.update();
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
                EditPlanActivity.open(this, planId, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
