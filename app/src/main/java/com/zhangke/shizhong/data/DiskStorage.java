package com.zhangke.shizhong.data;

import java.io.File;

/**
 * 硬盘存储模块提供的接口
 * Created by ZhangKe on 2018/4/12.
 */

public interface DiskStorage {

    /**
     * 获取存储路径
     */
    String getStoragePath();

    /**
     * 清除整个文件夹
     */
    String clearAll();
    
    String saveFile(String key, File file);
}
