package com.zhangke.shizhong.page.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.common.CustomFragmentPagerAdapter;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.page.plan.EditPlanFragment;
import com.zhangke.shizhong.page.plan.ShowPlanFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private CustomFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected int getLayoutResId() {
        initTheme();
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, getResources().getString(R.string.app_name), false);

        initFragment();
    }

    private void initTheme() {
        if (APPConfig.getTheme() == 0) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
    }

    private void initFragment() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ShowPlanFragment());
        fragmentList.add(new EditPlanFragment());
        fragmentList.add(new SettingFragment());
        fragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.img_show_plan, R.id.img_edit_plan, R.id.img_setting})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.img_show_plan:
                viewPager.setCurrentItem(0);
                break;
            case R.id.img_edit_plan:
                viewPager.setCurrentItem(1);
                break;
            case R.id.img_setting:
                viewPager.setCurrentItem(2);
                break;
        }
    }
}
