package com.zhangke.shizhong.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhangke.zlog.ZLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 具有倒计时功能的 TextView
 * <p>
 * Created by ZhangKe on 2018/5/27.
 */
public class CountDownTextView extends AppCompatTextView {

    private static final String TAG = "CountDownTextView";

    private long targetTimeMillis = 0L;

    private boolean destroyed = false;

    private CountDownThread countDownThread;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (msg.obj instanceof String) {
                    setText((String) msg.obj);
                }
            }
        }
    };

    private class CountDownThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!destroyed) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = computeDateTime();
                mHandler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ZLog.e(TAG, "CountDownThread", e);
                    if (destroyed) {
                        return;
                    } else {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        private String computeDateTime() {
            String countDownTime = "";
            if (targetTimeMillis != 0) {
                long diff = Math.abs(targetTimeMillis - System.currentTimeMillis());
                long month = diff / 1000L / 60L / 60L / 24L / 30L;
                long day = diff / 1000L / 60L / 60L / 24L % 30L;
                long hour = diff / 1000L / 60L / 60L - month * 30 * 24 - day * 24;
                long minute = diff / 1000L / 60L - month * 30 * 24 * 60 - day * 24 * 60 - hour * 60;
                long second = diff / 1000L - month * 30 * 24 * 60 * 60L - day * 24 * 60 * 60L - hour * 60 * 60L - minute * 60L;
                StringBuilder timeBuilder = new StringBuilder();
                if (month != 0) {
                    timeBuilder.append(month);
                    timeBuilder.append("个月");
                }
                if (day != 0) {
                    timeBuilder.append(day);
                    timeBuilder.append("天");
                }
                if (month != 0 || day != 0) {
                    timeBuilder.append("  ");
                }
                if (hour != 0) {
                    timeBuilder.append(hour);
                    timeBuilder.append("小时");
                }
                if (minute != 0) {
                    timeBuilder.append(minute);
                    timeBuilder.append("分");
                }
                if (second != 0) {
                    timeBuilder.append(second);
                    timeBuilder.append("秒");
                }
                countDownTime = timeBuilder.toString();
            }
            return countDownTime;
        }
    }

    public CountDownTextView(Context context) {
        super(context);
        init();
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, 0);
        init();
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
        init();
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
    }

    private void init() {
        destroyed = false;
        startCountDownThread();
    }

    private synchronized void startCountDownThread() {
        if (countDownThread == null) {
            countDownThread = new CountDownThread();
            countDownThread.start();
        }
    }

    /**
     * 设置目标日期
     */
    public void setTargetDate(String targetDate, String format) {
        try {
            Date date = new SimpleDateFormat(format, Locale.CHINA).parse(targetDate);
            targetTimeMillis = date.getTime();
        } catch (Exception e) {
            ZLog.e(TAG, "setTargetDate()", e);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        destroyed = false;
        startCountDownThread();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyed = true;
        if (countDownThread != null) {
            if (countDownThread.isAlive()) {
                countDownThread.interrupt();
            }
            countDownThread = null;
        }
    }
}
