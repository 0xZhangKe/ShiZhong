package com.zhangke.shizhong.page.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;

/**
 * 启动页面
 *
 * Created by ZhangKe on 2018/7/29.
 */
public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        fullScreen();
        return R.layout.activity_launch;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }
}
