package com.zhangke.shizhong.file;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存储保存文件的队列
 *
 * Created by ZhangKe on 2018/4/15.
 */

public class FileSaveQueue {

    private BlockingQueue<File> fileQueue = new LinkedBlockingQueue<>();

    public void add(File file){
        fileQueue.add(file);
    }

    public void start(){

    }
}
