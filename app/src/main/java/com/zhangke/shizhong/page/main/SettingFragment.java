package com.zhangke.shizhong.page.main;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.page.application.ApplicationStatisticsActivity;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.page.poster.inputname.InputNameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 设置界面
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class SettingFragment extends BaseFragment {

    @BindView(R.id.switch_compat)
    SwitchCompat switchCompat;
    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        initTheme();
        return R.layout.setting_fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, rootView);

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            changeTheme();
        });
    }

    private void initTheme() {
        if (APPConfig.getTheme() == 0) {
            mActivity.setTheme(R.style.NightTheme);
        } else {
            mActivity.setTheme(R.style.DayTheme);
        }
    }

    private void changeTheme() {
        if (APPConfig.getTheme() == 0) {
            APPConfig.setTheme(1);
            mActivity.setTheme(R.style.DayTheme);
        } else {
            APPConfig.setTheme(0);
            mActivity.setTheme(R.style.NightTheme);
        }
    }

    @OnClick({R.id.tv_douban_movie_poster, R.id.tv_music_poster, R.id.tv_app_manager})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_douban_movie_poster: {
                Intent intent = new Intent(mActivity, InputNameActivity.class);
                intent.putExtra(INTENT_ARG_01, 0);
                startActivity(intent);
                return;
            }
            case R.id.tv_music_poster: {
                Intent intent = new Intent(mActivity, InputNameActivity.class);
                intent.putExtra(INTENT_ARG_01, 1);
                startActivity(intent);
                return;
            }
            case R.id.tv_app_manager: {
                Intent intent = new Intent(mActivity, ApplicationStatisticsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
