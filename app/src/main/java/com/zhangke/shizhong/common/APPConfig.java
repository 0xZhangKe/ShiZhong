package com.zhangke.shizhong.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhangke.shizhong.util.FileUtils;
import com.zhangke.zlog.ZLog;

import java.io.File;

/**
 * app 配置文件
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class APPConfig {

    private static final String TAG = "APPConfig";

    private static final String SP_NAME = "UserConfig";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * APP数据存储跟目录名
     */
    private static final String STORAGE_ZS_ROOT_NAME = "始终";

    /**
     * 存储在 SharedPreferences 的APP主题配置字段key
     */
    private static final String THEME_SP_KEY = "theme";

    /**
     * APP当前主题</br>
     * -1-未初始化</br>
     * 0-夜间</br>
     * 1-日间
     */
    private static int theme = -1;

    /**
     * 获取 APP 主题
     *
     * @return 0-夜间，1-日间
     */
    public static int getTheme() {
        if (theme == -1) {
            getSharedPreferencesInstance();
            theme = sharedPreferences.getInt(THEME_SP_KEY, 0);
        }
        return theme;
    }

    public static void setTheme(int newTheme) {
        APPConfig.theme = newTheme;
        getSharedPreferencesEditorInstance();
        editor.putInt(THEME_SP_KEY, newTheme);
        editor.commit();
    }

    private static void getSharedPreferencesInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = SZApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    private static void getSharedPreferencesEditorInstance() {
        if (editor == null) {
            getSharedPreferencesInstance();
            editor = sharedPreferences.edit();
        }
    }

    /**
     * 获取豆瓣电影海报存储路径
     */
    public static File getMoviePosterRootFile() {
        File file = new File(String.format("%s/%s/豆瓣电影海报/", FileUtils.getStorageRootPath(), STORAGE_ZS_ROOT_NAME));
        if (!file.exists()) {
            File parentFile = new File(file.getParent());
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    ZLog.e(TAG, String.format("文件%s创建失败", parentFile.getPath()));
                }
            }
            if (!file.mkdirs()) {
                ZLog.e(TAG, String.format("文件%s创建失败", file.getPath()));
            }
        }
        return file;
    }

    /**
     * 获取云音乐海报存储路径
     */
    public static File getMusicPosterRootFile() {
        File file = new File(String.format("%s/%s/云音乐海报/", FileUtils.getStorageRootPath(), STORAGE_ZS_ROOT_NAME));
        if (!file.exists()) {
            File parentFile = new File(file.getParent());
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    ZLog.e(TAG, String.format("文件%s创建失败", parentFile.getPath()));
                }
            }
            if (!file.mkdirs()) {
                ZLog.e(TAG, String.format("文件%s创建失败", file.getPath()));
            }
        }
        return file;
    }
}
