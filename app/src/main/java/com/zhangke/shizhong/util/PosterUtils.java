package com.zhangke.shizhong.util;

import android.text.TextUtils;

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

    /**
     * 通过歌名及歌手名获取文件名
     */
    public static File getMusicFileWithName(String albumName, String musicName, String userName) {
        if (!TextUtils.isEmpty(albumName)) {
            albumName = albumName.replaceAll("\\\\", "-");
            albumName = albumName.replaceAll(" ", "-");
        } else {
            albumName = "其他";
        }
        if (!TextUtils.isEmpty(musicName)) {
            musicName = musicName.replaceAll("\\\\", "-");
            musicName = musicName.replaceAll(" ", "-");
        }
        if (!TextUtils.isEmpty(userName)) {
            userName = userName.replaceAll("\\\\", "-");
            userName = userName.replaceAll(" ", "-");
        }
        File parentFile = new File(String.format("%s/%s", APPConfig.getMusicPosterRootFile(), albumName));
        if (!parentFile.exists()) {
            if (parentFile.mkdirs()) {
                return new File(parentFile, String.format("%s-%s.jpg", musicName, userName));
            } else {
                return new File(APPConfig.getMusicPosterRootFile(), String.format("%s-%s.jpg", musicName, userName));
            }
        } else {
            return new File(parentFile, String.format("%s-%s.jpg", musicName, userName));
        }
    }
}
