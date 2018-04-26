package com.zhangke.shizhong.db;

import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * APP 数据实体
 * Created by ZhangKe on 2018/4/26.
 */
@Entity
public class ApplicationInfo implements Comparable<ApplicationInfo> {

    @Id(autoincrement = true)
    private long id;
    private String appName; // 应用名
    private String packageName; // 包名
    private String versionName; // 版本名
    private int versionCode = 0; // 版本号

    @Transient
    private Drawable appIcon = null; // 应用图标
    private String sortTarget;
    private boolean isSystemApp = false;

    @Generated(hash = 1914574166)
    public ApplicationInfo(long id, String appName, String packageName, String versionName,
            int versionCode, String sortTarget, boolean isSystemApp) {
        this.id = id;
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.sortTarget = sortTarget;
        this.isSystemApp = isSystemApp;
    }

    @Generated(hash = 126628142)
    public ApplicationInfo() {
    }

    @Override
    public String toString() {
        return appName + " , " + packageName + " ," + versionName + " ," + versionCode;
    }

    @Override
    public int compareTo(ApplicationInfo arg0) {
        return this.sortTarget.compareTo(arg0.sortTarget);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getSortTarget() {
        return this.sortTarget;
    }

    public void setSortTarget(String sortTarget) {
        this.sortTarget = sortTarget;
    }

    public boolean getIsSystemApp() {
        return this.isSystemApp;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }
}
