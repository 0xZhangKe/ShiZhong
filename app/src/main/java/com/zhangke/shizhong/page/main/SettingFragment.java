package com.zhangke.shizhong.page.main;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.page.application.ApplicationStatisticsActivity;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.page.other.WifiInfoActivity;
import com.zhangke.shizhong.page.poster.InputNameActivity;
import com.zhangke.shizhong.widget.RippleAnimationView;

import org.greenrobot.eventbus.EventBus;

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

    @BindView(R.id.nested_root_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.switch_compat)
    SwitchCompat switchCompat;
    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, rootView);

        initSwitch();
    }

    private void initSwitch() {
        if (APPConfig.getTheme() == 0) {
            mActivity.setTheme(R.style.NightTheme);
            switchCompat.setChecked(true);
            switchCompat.setText("夜间");
        } else {
            mActivity.setTheme(R.style.DayTheme);
            switchCompat.setChecked(false);
            switchCompat.setText("日间");
        }
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            changeTheme();
        });
    }

    private void changeTheme() {
        if (APPConfig.getTheme() == 0) {
            APPConfig.setTheme(1);
            mActivity.setTheme(R.style.DayTheme);
            switchCompat.setText("日间");
        } else {
            APPConfig.setTheme(0);
            mActivity.setTheme(R.style.NightTheme);
            switchCompat.setText("夜间");
        }
        RippleAnimationView.create(switchCompat).setDuration(400).start();
        EventBus.getDefault().post(new ThemeChangedEvent());
    }

    @OnClick({R.id.tv_douban_movie_poster, R.id.tv_music_poster,
            R.id.tv_app_manager, R.id.tv_wifi,
            R.id.tv_qr_code})
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
            case R.id.tv_wifi: {
                startActivity(new Intent(mActivity, WifiInfoActivity.class));
                break;
            }
            case R.id.tv_qr_code:{

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
