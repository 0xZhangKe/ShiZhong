package com.zhangke.shizhong.presenter.plan;

import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.util.DateUtils;
import com.zhangke.zlog.ZLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 计划相关的帮助类
 * <p>
 * Created by ZhangKe on 2018/5/21.
 */
public class PlanHelper {

    private static final String TAG = "PlanHelper";

    private static String curDate = DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");

    /**
     * 计判断此记录是否属于当前周期
     *
     * @param periodType 0-天，1-周，2-月
     */
    public static boolean isCurPeriod(int periodType, ClockRecord record) {
        boolean current = false;
        switch (periodType) {
            case 0:
                if (DateUtils.compareDate("yyyy-MM-dd HH:mm:ss", curDate, record.getDate()) == 0) {
                    current = true;
                }
                break;
            case 1: {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                try {
                    c1.setTime(sdf.parse(curDate));
                    c2.setTime(sdf.parse(record.getDate()));
                    current = c2.get(Calendar.WEEK_OF_YEAR) == c1.get(Calendar.WEEK_OF_YEAR);
                } catch (ParseException e) {
                    ZLog.e(TAG, "isCurPeriod()", e);
                    current = false;
                }
                break;
            }
            case 2: {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                try {
                    c1.setTime(sdf.parse(curDate));
                    c2.setTime(sdf.parse(record.getDate()));
                    current = c2.get(Calendar.MONTH) == c1.get(Calendar.MONTH);
                } catch (ParseException e) {
                    ZLog.e(TAG, "isCurPeriod()", e);
                    current = false;
                }
                break;
            }
        }
        return current;
    }
}
