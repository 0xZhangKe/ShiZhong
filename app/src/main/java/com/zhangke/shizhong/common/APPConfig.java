package com.zhangke.shizhong.common;

import com.zhangke.shizhong.util.FileUtils;

import java.io.File;

/**
 * app 配置文件
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class APPConfig {

    /**
     * APP数据存储跟目录名
     */
    public static final String STORAGE_ZS_ROOT_NAME = "始终";

    /**
     * 获取豆瓣电影海报存储路径
     */
    public static File getMoviewPosterRootFile() {
        File file = new File(String.format("%s/%s/豆瓣电影海报/", FileUtils.getStorageRootPath(), STORAGE_ZS_ROOT_NAME));
        if (!file.exists()) {
            if (!new File(file.getParent()).exists()) {
                new File(file.getParent()).mkdirs();
            }
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取云音乐海报存储路径
     */
    public static File getMusicPosterRootFile() {
        File file = new File(String.format("%s/%s/云音乐海报/", FileUtils.getStorageRootPath(), STORAGE_ZS_ROOT_NAME));
        if (!file.exists()) {
            if (!new File(file.getParent()).exists()) {
                new File(file.getParent()).mkdirs();
            }
            file.mkdirs();
        }
        return file;
    }
}
