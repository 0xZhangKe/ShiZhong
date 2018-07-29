package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.haibin.calendarview.CalendarView;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IClockPlanDetailContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.presenter.plan.ClockPlanDetailPresenter;
import com.zhangke.shizhong.util.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 打卡计划详情
 * <p>
 * Created by ZhangKe on 2018/7/29.
 */
public class ClockPlanDetailActivity extends BaseActivity implements IClockPlanDetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_select_month)
    TextView tvSelectMonth;

    private long planId;

    private String selectMonth;

    private IClockPlanDetailContract.Presenter presenter;

    private List<ClockRecord> listData = new ArrayList<>();
    private ClockPlanDetailAdapter adapter;

    public static void open(Context context, long planId) {
        Intent intent = new Intent(context, ClockPlanDetailActivity.class);
        intent.putExtra(INTENT_ARG_01, planId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_clock_plan_detail;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar(toolbar, "详细内容", true);

        planId = getIntent().getLongExtra(INTENT_ARG_01, -1L);

        selectMonth = DateUtils.getCurrentDate("yyyy-MM");

        adapter = new ClockPlanDetailAdapter(this, listData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter = new ClockPlanDetailPresenter(this, planId);
        presenter.update(selectMonth);

        calendarView.setOnMonthChangeListener((int year, int month) -> {
            selectMonth = String.format("%s-%s", year, month);
            presenter.update(selectMonth);
        });
    }

    @Override
    public void updateClockPlan(ClockPlan clockPlan) {

    }

    @Override
    public void notifyRecordChanged(List<ClockRecord> list) {
        listData.clear();
        listData.addAll(list);
        adapter.notifyDataSetChanged();
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
                EditPlanActivity.open(this, planId, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PlanChangedEvent event) {
        presenter.clearCache();
        presenter.update(selectMonth);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
