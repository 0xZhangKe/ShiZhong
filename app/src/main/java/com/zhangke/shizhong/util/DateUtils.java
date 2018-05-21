package com.zhangke.shizhong.util;

import android.text.TextUtils;

import com.zhangke.zlog.ZLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期相关工具
 *
 * Created by ZhangKe on 2018/5/21.
 */
public class DateUtils {

    private static final String TAG = "DateUtils";

    /**
     * 获取当前日期
     *
     * @param format 日期格式，如HH:mm
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 比较两个日期的大小.
     *
     * @param format 日期格式
     * @return 后面的时间大于前面的时间时返回 -1，日期相等返回0，否则返回1。
     */
    public static int compareDate(String format, String mDate, String strAnotherDate) {
        if (TextUtils.equals(mDate, strAnotherDate)) {
            return 0;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date currentDate;
        Date anotherDate;
        try {
            currentDate = simpleDateFormat.parse(mDate);
            anotherDate = simpleDateFormat.parse(strAnotherDate);
            return currentDate.compareTo(anotherDate);
        } catch (ParseException e) {
            ZLog.e(TAG, "compareDate()", e);
            return 1;
        }
    }

}
