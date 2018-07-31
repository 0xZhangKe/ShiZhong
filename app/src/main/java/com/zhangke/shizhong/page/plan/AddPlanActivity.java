package com.zhangke.shizhong.page.plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.CustomFragmentPagerAdapter;
import com.zhangke.shizhong.event.PlanSelectedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加计划界面
 * <p>
 * Created by ZhangKe on 2018/5/19.
 */
public class AddPlanActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_plan;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar(toolbar, "添加计划", true);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ChoosePlanTypeFragment());
        fragmentList.add(new AddPlanFragment());

        CustomFragmentPagerAdapter fragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PlanSelectedEvent event) {
        if(event.getPlanType() == -1) {
            viewPager.setCurrentItem(0);
        }else{
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 1){
            EventBus.getDefault().post(new PlanSelectedEvent(-1));
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
