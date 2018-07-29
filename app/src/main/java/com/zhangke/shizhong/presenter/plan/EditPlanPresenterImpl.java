package com.zhangke.shizhong.presenter.plan;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.zhangke.shizhong.contract.plan.IEditPlanContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockPlanDao;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationPlanDao;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.model.plan.EditPlanDataEntity;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;
import com.zhangke.shizhong.util.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改计划
 * <p>
 * Created by ZhangKe on 2018/7/20.
 */
public class EditPlanPresenterImpl implements IEditPlanContract.Presenter {

    private Context context;
    private IEditPlanContract.View view;

    private long planId;
    /**
     * 0-定量计划
     * 1-打卡计划
     */
    private int planType;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private RationPlanDao mRationPlanDao;
    private ClockPlanDao mClockPlanDao;

    private RationPlan rationPlan;
    private ClockPlan clockPlan;

    public EditPlanPresenterImpl(Context context, IEditPlanContract.View view) {
        this.context = context;
        this.view = view;

        mRationPlanDao = DBManager.getInstance().getRationPlanDao();
        mClockPlanDao = DBManager.getInstance().getClockPlanDao();
    }

    @Override
    public void initDate(long planId, int planType) {
        this.planId = planId;
        this.planType = planType;
        view.showRoundProgressDialog();
        if (planType == 0) {
            view.showRationPlan();
            getRationPlanWithId();
        } else {
            view.showClockPlan();
            getClockPlanWithId();
        }
    }

    private void getRationPlanWithId() {
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
                .subscribe((Integer value) -> {
                    if (value == 0) {
                        planDoesNotExits();
                    } else {
                        mHandler.postDelayed(() -> view.fillRationPlanData(rationPlan), 500);
                    }
                });
    }

    private void getClockPlanWithId() {
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
                .subscribe((Integer value) -> {
                    if (value == 0) {
                        planDoesNotExits();
                    } else {
                        mHandler.postDelayed(() -> view.fillClockPlanData(clockPlan), 500);
                    }
                });
    }

    private void planDoesNotExits() {
        view.showNoActionSnackbar("计划不存在啊？怎么回事");
        mHandler.postDelayed(() -> ((Activity) view).finish(), 700);
    }

    @Override
    public void updatePlan(final EditPlanDataEntity editData) {
        if (planType == 0) {
            if (DateUtils.compareDate("yyyy-MM-dd", rationPlan.getStartDate(), editData.getFinishDate()) != -1) {
                view.showNoActionSnackbar("结束时间不能小于开始时间！！！");
                return;
            }
            Observable.create((ObservableEmitter<Integer> e) -> {
                rationPlan.setName(editData.getName());
                rationPlan.setTarget(editData.getTarget());
                rationPlan.setCurrent(editData.getCurrent());
                rationPlan.setFinishDate(editData.getFinishDate());
                rationPlan.setUnit(editData.getUnit());
                rationPlan.setPeriodIsOpen(editData.isPeriodIsOpen());
                if(editData.isPeriodIsOpen()){
                    rationPlan.setPeriodPlanTarget(editData.getPeriodTarget());
                    rationPlan.setPeriodPlanType(editData.getPeriodPlanType());
                }
                mRationPlanDao.insertOrReplace(rationPlan);
                DBManager.getInstance().clear();
                e.onNext(0);
                e.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> editSuccess());
        } else {
            Observable.create((ObservableEmitter<Integer> e) -> {
                clockPlan.setName(editData.getName());
                if (!TextUtils.isEmpty(editData.getDescription())) {
                    clockPlan.setDescription(editData.getDescription());
                }
                mClockPlanDao.insertOrReplace(clockPlan);
                DBManager.getInstance().clear();
                e.onNext(0);
                e.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> editSuccess());
        }
    }

    @Override
    public void deletePlan() {
        if (planType == 0) {
            mRationPlanDao.deleteByKey(planId);
        } else {
            mClockPlanDao.deleteByKey(planId);
        }
        view.showNoActionSnackbar("搞定");
        DBManager.getInstance().clear();
        EventBus.getDefault().post(new PlanChangedEvent());
        mHandler.postDelayed(() -> ((Activity) context).finish(), 700);
    }

    @Override
    public void deletePeriod() {
        if (rationPlan != null) {
            rationPlan.setPeriodIsOpen(false);
            mRationPlanDao.insertOrReplace(rationPlan);
            DBManager.getInstance().clear();
            initDate(planId, planType);
        }
    }

    private void editSuccess() {
        view.showNoActionSnackbar("修改好了");
        EventBus.getDefault().post(new PlanChangedEvent());
        mHandler.postDelayed(() -> ((Activity) context).finish(), 700);
    }

    @Override
    public void onDestroy() {
        mRationPlanDao = null;
        mClockPlanDao = null;
    }
}
