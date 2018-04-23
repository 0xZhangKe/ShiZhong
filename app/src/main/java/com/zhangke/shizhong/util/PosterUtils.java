package com.zhangke.shizhong.util;

import com.zhangke.shizhong.common.APPConfig;

import java.io.File;

/**
 * 海报操作相关方法
 * <p>
 * Created by ZhangKe on 2018/4/22.
 */

public class PosterUtils {

    /**
     * 通过电影名获取海报存储地址
     */
    public static File getMoviePosterFileWithName(File rootPath, String movieName) {
        movieName = movieName.replaceAll("\\\\", "-");
        movieName = movieName.replaceAll(" ", "-");
        return new File(rootPath, String.format("%s.jpg", movieName));
    }

    /**
     * 通过电影名获取海报存储地址
     */
    public static File getMoviePosterFileWithName(String movieName) {
        return getMoviePosterFileWithName(APPConfig.getMoviePosterRootFile(), movieName);
    }
}
