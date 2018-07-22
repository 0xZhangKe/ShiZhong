package com.zhangke.shizhong.presenter.plan;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zhangke.shizhong.contract.plan.IPlanDetailContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockPlanDao;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationPlanDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 计划详情
 * <p>
 * Created by ZhangKe on 2018/7/22.
 */
public class PlanDetailPresenterImpl implements IPlanDetailContract.Presenter {

    private Context context;
    private IPlanDetailContract.View view;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private RationPlanDao mRationPlanDao;
    private ClockPlanDao mClockPlanDao;

    private RationPlan rationPlan;
    private ClockPlan clockPlan;

    public PlanDetailPresenterImpl(Context context, IPlanDetailContract.View view) {
        this.context = context;
        this.view = view;

        mRationPlanDao = DBManager.getInstance().getRationPlanDao();
        mClockPlanDao = DBManager.getInstance().getClockPlanDao();
    }

    @Override
    public void initDate(long planId, int planType) {
        if (planType == 0) {
            view.showRationPlan();
            getRationPlanWithId(planId);
        } else {
            view.showClockPlan();
            getClockPlanWithId(planId);
        }
    }

    private void getRationPlanWithId(final long planId) {
        Observable.create((ObservableEmitter<Integer> e) -> {
            List<RationPlan> rationPlanList = mRationPlanDao.queryBuilder()
                    .where(RationPlanDao.Properties.Id.eq(planId))
                    .list();
            if (rationPlanList.isEmpty()) {
                e.onNext(0);
            } else {
                rationPlan = rationPlanList.get(0);
                e.onNext(1);
            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (value == 0) {
                        planDoesNotExits();
                    } else {
                        mHandler.postDelayed(() -> view.fillRationPlanData(rationPlan), 500);
                    }
                });
    }

    private void getClockPlanWithId(final long planId) {
        Observable.create((ObservableEmitter<Integer> e) -> {
            List<ClockPlan> clockPlanList = mClockPlanDao.queryBuilder()
                    .where(ClockPlanDao.Properties.Id.eq(planId))
                    .list();
            if (clockPlanList.isEmpty()) {
                e.onNext(0);
            } else {
                clockPlan = clockPlanList.get(0);
                e.onNext(1);
            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (value == 0) {
                        planDoesNotExits();
                    } else {
                        mHandler.postDelayed(() -> view.fillClockPlanData(clockPlan), 500);
                    }
                });
    }

    private void planDoesNotExits() {
        view.showNoActionSnackbar("计划不存在，刚刚被你删了吧？");
        mHandler.postDelayed(() -> ((Activity) view).finish(), 700);
    }

    @Override
    public void onDestroy() {
        mClockPlanDao = null;
        mRationPlanDao = null;
    }
}
