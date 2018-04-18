package com.zhangke.shizhong.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * HTTP 工具类
 *
 * Created by ZhangKe on 2018/4/18.
 */

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static String encode(String text){
        try{
            text = URLEncoder.encode(text, "utf-8");
        } catch(UnsupportedEncodingException e){
            Log.e(TAG, e.toString());
        }
        return text;
    }
}
