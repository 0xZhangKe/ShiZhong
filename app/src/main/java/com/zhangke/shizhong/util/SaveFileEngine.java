package com.zhangke.shizhong.util;

import android.graphics.Bitmap;
import android.os.Process;
import android.util.Log;

import com.zhangke.zlog.ZLog;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 负责将图片保存到文件中，适用于高并发场景。
 * <p>
 * Created by ZhangKe on 2018/4/23.
 */
public class SaveFileEngine implements ISaveFileEngine {

    private static final String TAG = "SaveFileEngine";

    private LinkedBlockingQueue<FileBitmapBean> mBitmapQueue = new LinkedBlockingQueue<>();
    private boolean exited = false;
    private SaveFileThread saveFileThread;

    private class SaveFileThread extends Thread {

        @Override
        public void run() {
            super.run();
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            while (!exited) {
                try {
                    FileBitmapBean item = mBitmapQueue.take();
                    FileUtils.saveBitmapToDisk(item.file, item.bitmap);
                } catch (InterruptedException e) {
                    if (exited) {
                        return;
                    }
                }
            }
        }
    }

    public SaveFileEngine() {
        exited = false;
        saveFileThread = new SaveFileThread();
        saveFileThread.start();
    }

    @Override
    public void addFile(File file, Bitmap bitmap) {
        if (!mBitmapQueue.offer(new FileBitmapBean(file, bitmap))) {
            ThreadPool.getInstance().getThreadPool().execute(() -> {
                try {
                    mBitmapQueue.put(new FileBitmapBean(file, bitmap));
                } catch (InterruptedException e) {
                    ZLog.e(TAG, "run: ", e);
                }
            });
        }
    }

    @Override
    public void exit() {
        exited = true;
        saveFileThread.interrupt();
    }

    private class FileBitmapBean {
        File file;
        Bitmap bitmap;

        FileBitmapBean(File file, Bitmap bitmap) {
            this.file = file;
            this.bitmap = bitmap;
        }
    }
}
