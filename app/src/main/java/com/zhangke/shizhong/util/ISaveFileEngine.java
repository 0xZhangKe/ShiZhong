package com.zhangke.shizhong.util;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 负责将一个 Bitmap 保存到文件中
 * Created by ZhangKe on 2018/4/23.
 */
public interface ISaveFileEngine {

    /**
     * 添加一个需要保存的图片
     */
    void addFile(File file, Bitmap bitmap);

    /**
     * 结束
     */
    void exit();
}
