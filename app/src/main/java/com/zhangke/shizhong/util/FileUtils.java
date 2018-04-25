package com.zhangke.shizhong.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.zlog.ZLog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作相关工具
 * <p>
 * Created by ZhangKe on 2018/4/22.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";

    /**
     * 获取存储跟路径
     */
    public static File getStorageRootPath() {
        File file = Environment.getExternalStorageDirectory();
        if (file == null) {
            file = getExtSDCardPath();
        }
        if (file == null) {
            file = SZApplication.getInstance().getExternalFilesDir(null);
        }
        return file;
    }

    private static File getExtSDCardPath() {
        List<String> sdcardList = new ArrayList<>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("media"))
                    continue;
                if (line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data")
                        || line.contains("tmpfs") || line.contains("shell")
                        || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc")
                        || line.contains("obb")) {
                    continue;
                }

                if (line.contains("fat") || line.contains("fuse") || (line
                        .contains("ntfs"))) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        String path = columns[1];
                        if (path != null && !sdcardList.contains(path) && path.contains("sd"))
                            sdcardList.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            ZLog.e(TAG, e.toString());
        }

        if (!sdcardList.isEmpty()) {
            return new File(sdcardList.get(0));
        } else {
            return null;
        }
    }

    /**
     * 将图片保存到文件中
     */
    public static boolean saveBitmapToDisk(File file, Bitmap originBitmap) {
        boolean success = false;
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                        originBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        success = true;
                    } catch (IOException e) {
                        ZLog.e(TAG, "saveBitmapToDisk: ", e);
                    }
                }
            }
        } catch (Exception e) {
            ZLog.e(TAG, "saveBitmapToDisk: ", e);
        }
        return success;
    }
}
