package com.zhangke.shizhong.common;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Application
 * <p>
 * Created by ZhangKe on 2018/4/12.
 */

public class SZApplication extends MultiDexApplication {

    private static final String TAG = "ZLDApplication";
    private static SZApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

    }

    public static SZApplication getInstance() {
        return application;
    }
}
