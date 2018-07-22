package com.zhangke.shizhong.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.DatePicker;

import com.zhangke.zlog.ZLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 事件选择器
 * Created by ZhangKe on 2018/7/22.
 */
public class DateTimePickerHelper {

    private static final String TAG = "DateTimePickerHelper";

    public static void showDateDialog(Context context,
                                      final String type,
                                      final OnCallbackListener onCallbackListener) {
        showDateDialog(context, type, "", "", "", onCallbackListener);
    }

    public static void showDateDialog(Context context,
                                      final String type,
                                      final String selectDate,
                                      final OnCallbackListener onCallbackListener) {
        showDateDialog(context, type, "", "", selectDate, onCallbackListener);
    }

    /**
     * 显示日期对话框
     *
     * @param type       日期格式
     * @param beginDate  可选择的最小日期，没有传空
     * @param endDate    可选择的最大日期，没有传空
     * @param selectDate 当前已选择日期， 没有传空
     */
    public static void showDateDialog(Context context,
                                      final String type,
                                      String beginDate,
                                      String endDate,
                                      String selectDate,
                                      final OnCallbackListener onCallbackListener) {
        Calendar cal = Calendar.getInstance();
        final DatePickerDialog mDialog = new DatePickerDialog(context, null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type, Locale.CHINA);
        if (!TextUtils.isEmpty(beginDate)) {
            try {
                mDialog.getDatePicker().setMinDate(simpleDateFormat.parse(beginDate).getTime());
            } catch (ParseException e) {
                ZLog.e(TAG, "showDateDialog()", e);
            }
            if (!TextUtils.isEmpty(selectDate)) {
                if (DateUtils.compareDate(type, selectDate, beginDate) == -1) {
                    selectDate = beginDate;
                }
            }
        }
        if (!TextUtils.isEmpty(endDate)) {
            try {
                mDialog.getDatePicker().setMaxDate(simpleDateFormat.parse(endDate).getTime());
            } catch (ParseException e) {
                ZLog.e(TAG, "showDateDialog", e);
            }
            if (!TextUtils.isEmpty(selectDate)) {
                if (DateUtils.compareDate(type, endDate, selectDate) == -1) {
                    selectDate = endDate;
                }
            }
        }
        if (!TextUtils.isEmpty(selectDate)) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(simpleDateFormat.parse(selectDate));
            } catch (ParseException e) {
                ZLog.e(TAG, "showDateDialog", e);
            }
            mDialog.getDatePicker().init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    null);
        }
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = mDialog.getDatePicker();
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                SimpleDateFormat sdf;
                if (TextUtils.isEmpty(type)) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                } else {
                    sdf = new SimpleDateFormat(type, Locale.CHINA);
                }
                String dateTime = sdf.format(calendar.getTime());
                onCallbackListener.onCallback(dateTime);
            }
        });
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mDialog.show();
    }

    public interface OnCallbackListener {
        void onCallback(String date);
    }
}
