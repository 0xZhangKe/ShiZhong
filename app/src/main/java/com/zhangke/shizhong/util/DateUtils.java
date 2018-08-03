package com.zhangke.shizhong.util;

import android.text.TextUtils;
import android.util.Log;

import com.zhangke.zlog.ZLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期相关工具
 * <p>
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

    /**
     * 获取两个日期相差的月份
     */
    public static int getMonthSpace(String format, String startDate, String endDate) {
        int monthSpace = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(startDate));
            c2.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            Log.e(TAG, "getMonthSpace: ", e);
        }
        monthSpace = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        monthSpace += (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
        return Math.abs(monthSpace);
    }

    /**
     * 获取两个日期相差的天数
     */
    public static int getDaySpace(String format, String startDate, String endDate) {
        int daySpace = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(startDate));
            c2.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            Log.e(TAG, "getMonthSpace: ", e);
        }
        daySpace = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
        daySpace += (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 365;
        return Math.abs(daySpace);
    }

    /**
     * 获取两个日期相差的天数
     */
    public static int getWeekSpace(String format, String startDate, String endDate) {
        return getDaySpace(format, startDate, endDate) / 7;
    }

    /**
     * 获取两个日期相差的小时
     */
    public static int getHourSpace(String format, String startDate, String finishDate) {
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date beginDate = sdf.parse(startDate);
            Date endDate = sdf.parse(finishDate);
            result = Math.abs(((int) (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60)));
        } catch (ParseException e) {
            ZLog.e(TAG, "getHourSpace", e);
        }
        return result;
    }
}
