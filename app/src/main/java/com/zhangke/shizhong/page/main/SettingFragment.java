package com.zhangke.shizhong.page.main;

import android.content.Intent;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.page.poster.inputName.InputNameActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class SettingFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.setting_fragment;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, rootView);

    }

    @OnClick({R.id.tv_douban_movie_poster, R.id.tv_music_poster})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_douban_movie_poster: {
                Intent intent = new Intent(mActivity, InputNameActivity.class);
                startActivity(intent);
                return;
            }
            case R.id.tv_music_poster: {
                Intent intent = new Intent(mActivity, InputNameActivity.class);
                startActivity(intent);
                return;
            }
        }
    }
}
