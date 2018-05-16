package com.zhangke.shizhong.model.poster;

import android.widget.ImageView;

import java.io.File;

/**
 * 加载图片工具接口
 *
 * Created by ZhangKe on 2018/4/22.
 */

public interface ILoadBitmap {

    /**
     * 下载指定图片到 ImageView 中，并把这个图片下载到本地磁盘
     *
     * @param file 将要保存的文件位置
     */
    void loadBitmapIntoImageView(String url,
                                 ImageView imageView,
                                 File file);
}
