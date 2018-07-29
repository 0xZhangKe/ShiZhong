package com.zhangke.shizhong.presenter.plan;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.zhangke.shizhong.contract.plan.IClockPlanDetailContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockPlanDao;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.db.ClockRecordDao;
import com.zhangke.shizhong.db.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 打卡计划详情
 * <p>
 * Created by ZhangKe on 2018/7/29.
 */
public class ClockPlanDetailPresenter implements IClockPlanDetailContract.Presenter {

    private static final String TAG = "ClockPlanDetail";

    private IClockPlanDetailContract.View view;
    private long planId;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ClockPlanDao mClockPlanDao;
    private ClockPlan clockPlan;
    private List<ClockRecord> listData = new ArrayList<>();

    private String selectMonth;
    private Observable<Integer> observable;

    private Calendar mCalendar = Calendar.getInstance();
    private SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public ClockPlanDetailPresenter(IClockPlanDetailContract.View view, long planId) {
        this.view = view;
        this.planId = planId;

        mClockPlanDao = DBManager.getInstance().getClockPlanDao();

        observable = Observable.create(emitter -> {
            if (!listData.isEmpty()) {
                listData.clear();
            }
            if (clockPlan == null) {
                List<ClockPlan> planList = mClockPlanDao.queryBuilder()
                        .where(ClockPlanDao.Properties.Id.eq(planId))
                        .list();
                if (!planList.isEmpty()) {
                    clockPlan = planList.get(0);
                }
            }
            if (clockPlan == null) {
                emitter.onNext(0);
            } else {
                if (clockPlan.getClockRecords() != null && !clockPlan.getClockRecords().isEmpty()) {
                    listData.addAll(Observable.fromIterable(clockPlan.getClockRecords())
                            .filter(ClockPlanDetailPresenter.this::recordIsCurMonth)
                            .map(record -> {
                                if (TextUtils.isEmpty(record.getDescription())) {
                                    record.setDescription("其它");
                                }
                                return record;
                            })
                            .toList()
                            .blockingGet());
                }
                emitter.onNext(1);
            }
            emitter.onComplete();
        });
    }

    private boolean recordIsCurMonth(ClockRecord record) {
        Calendar recordCalendar = Calendar.getInstance();
        try {
            recordCalendar.setTime(fullDateFormat.parse(record.getDate()));
        } catch (ParseException e) {
            Log.e(TAG, "recordIsCurMonth: ", e);
            return true;
        }
        return (mCalendar.get(Calendar.YEAR) == recordCalendar.get(Calendar.YEAR)) &&
                (mCalendar.get(Calendar.MONTH) == recordCalendar.get(Calendar.MONTH));
    }

    @Override
    public void clearCache() {
        clockPlan = null;
    }

    @Override
    public synchronized void update(String month) {
        this.selectMonth = month;
        try {
            mCalendar.setTime(monthFormat.parse(selectMonth));
        } catch (ParseException e) {
            Log.e(TAG, "update: ", e);
        }
        observable.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (value == 0) {
                        planDoesNotExits();
                    } else {
                        view.updateClockPlan(clockPlan);
                        view.notifyRecordChanged(listData);
                    }
                });
    }

    private void planDoesNotExits() {
        view.showNoActionSnackbar("计划不存在啊？怎么回事");
        mHandler.postDelayed(() -> ((Activity) view).finish(), 1000);
    }
}
