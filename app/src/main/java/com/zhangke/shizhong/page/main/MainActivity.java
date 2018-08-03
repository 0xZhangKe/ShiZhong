package com.zhangke.shizhong.page.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.common.CustomFragmentPagerAdapter;
import com.zhangke.shizhong.event.PosterHideChangedEvent;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.page.todo.ShowTodoFragment;
import com.zhangke.shizhong.page.plan.ShowPlanFragment;
import com.zhangke.shizhong.util.ThemeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ll_root)
    ViewGroup llRoot;
//    @BindView(R.id.view_toolbar_divider)
//    View viewToolbarDivider;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.ll_tab_group)
    ViewGroup llTabGroup;
    @BindView(R.id.view_tab_divider)
    View viewTabDivider;

    private CustomFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initFragment();
    }

    private void initFragment() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ShowPlanFragment());
        fragmentList.add(new ShowTodoFragment());
        fragmentList.add(new SettingFragment());
        fragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private long lastClickTime = 0;
    private int switchCount = 0;

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
                if(System.currentTimeMillis() - lastClickTime < 300){
                    switchCount ++;
                }
                lastClickTime = System.currentTimeMillis();
                if(switchCount >= 3){
                    APPConfig.setPosterHide(!APPConfig.posterHide());
                    EventBus.getDefault().post(new PosterHideChangedEvent());
                    switchCount = 0;
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ThemeChangedEvent event) {
        refreshUI();
    }

    private void refreshUI(){
        ThemeUtils.refreshUI(this, llRoot);
        initStatusBar();
        TypedValue toolbarColor = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.toolbarColor, toolbarColor, true);
        llTabGroup.setBackgroundResource(toolbarColor.resourceId);

//        viewToolbarDivider.setBackground(getResources().getDrawable(R.drawable.toolbar_bottom_line));
        viewTabDivider.setBackground(getResources().getDrawable(R.drawable.tab_top_line));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
