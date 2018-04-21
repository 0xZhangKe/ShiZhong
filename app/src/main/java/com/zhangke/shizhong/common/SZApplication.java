package com.zhangke.shizhong.common;

import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;

import com.zhangke.shizhong.util.CrashHandler;
import com.zhangke.zlog.ZLog;

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

        ZLog.Init(String.format("%s/log/", getExternalFilesDir(null).getPath()));

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public static SZApplication getInstance() {
        return application;
    }

    public static boolean debug() {
        try {
            ApplicationInfo info = getInstance().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
