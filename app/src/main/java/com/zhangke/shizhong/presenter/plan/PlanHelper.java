package com.zhangke.shizhong.presenter.plan;

import android.text.TextUtils;

import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.db.RationPlan;
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
     * 计判断此日期是否属于当前周期
     *
     * @param periodType 0-天，1-周，2-月
     */
    public static boolean isCurPeriod(int periodType, String date, String dateFormat) {
        boolean current = false;
        switch (periodType) {
            case 0: {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                try {
                    c1.setTime(sdf.parse(curDate));
                    c2.setTime(sdf.parse(date));
                    current = c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR) && c2.get(Calendar.DAY_OF_YEAR) == c1.get(Calendar.DAY_OF_YEAR);
                } catch (ParseException e) {
                    ZLog.e(TAG, "isCurPeriod()", e);
                    current = false;
                }
                break;
            }
            case 1: {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                try {
                    c1.setTime(sdf.parse(curDate));
                    c2.setTime(sdf.parse(date));
                    current = c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR) && c2.get(Calendar.WEEK_OF_YEAR) == c1.get(Calendar.WEEK_OF_YEAR);
                } catch (ParseException e) {
                    ZLog.e(TAG, "isCurPeriod()", e);
                    current = false;
                }
                break;
            }
            case 2: {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                try {
                    c1.setTime(sdf.parse(curDate));
                    c2.setTime(sdf.parse(date));
                    current = c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR) && c2.get(Calendar.MONTH) == c1.get(Calendar.MONTH);
                } catch (ParseException e) {
                    ZLog.e(TAG, "isCurPeriod()", e);
                    current = false;
                }
                break;
            }
        }
        return current;
    }

    /**
     * 获取计划进度
     *
     * @return 0-100
     */
    public static int getProgress(RationPlan plan) {
        Double progress;
        progress = plan.getCurrent() / plan.getTarget() * 100;
        return progress.intValue();
    }

    /**
     * 获取计划类型
     *
     * @param plan 计划数据
     * @return 0-攒钱计划；1-减肥计划；10-其他计划
     */
    public static int getPlanType(RationPlan plan) {
        int planType = 10;
        if (plan.getPlanType() != -1) {
            planType = plan.getPlanType();
        } else {
            String planName = plan.getName();
            if (!TextUtils.isEmpty(planName)) {
                if ((plan.getName().contains("钱") || plan.getUnit().contains("元")) && plan.getTarget() > plan.getCurrent()) {
                    planType = 0;
                } else if ((plan.getName().contains("减肥") || plan.getName().contains("变瘦") || plan.getUnit().contains("斤")) && plan.getCurrent() > plan.getTarget()) {
                    planType = 1;
                }
            }
        }
        return planType;
    }

    /**
     * 计算短期计划目标值
     *
     * @param plan 定量计划实体
     * @param type 短期计划周期类型，0-日，1-周，2-月
     */
    public static double getPeriodTarget(RationPlan plan, int type) {
        double value = 0.0;
        switch (type) {
            case 0:
                value = (plan.getTarget() - plan.getCurrent()) / (DateUtils.getDaySpace("yyyy-MM-dd", plan.getStartDate(), plan.getFinishDate()) + 1);
                break;
            case 1:
                value = (plan.getTarget() - plan.getCurrent()) / (DateUtils.getWeekSpace("yyyy-MM-dd", plan.getStartDate(), plan.getFinishDate()) + 1);
                break;
            case 2:
                value = (plan.getTarget() - plan.getCurrent()) / (DateUtils.getMonthSpace("yyyy-MM-dd", plan.getStartDate(), plan.getFinishDate()) + 1);
                break;
        }
        return value;
    }
}
