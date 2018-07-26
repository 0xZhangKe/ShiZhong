package com.zhangke.shizhong.presenter.plan;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.zhangke.shizhong.contract.plan.IRationPlanDetailContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationPlanDao;
import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.db.RationRecordDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 定量计划详情
 * <p>
 * Created by ZhangKe on 2018/7/26.
 */
public class RationPlanDetailPresenterImpl implements IRationPlanDetailContract.Presenter {

    private IRationPlanDetailContract.View view;
    private long planId;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private RationPlanDao mRationPlanDao;
    private RationRecordDao mRationRecordDao;

    private List<RationRecord> listData = new ArrayList<>();

    private RationPlan rationPlan;

    public RationPlanDetailPresenterImpl(IRationPlanDetailContract.View view, long planId) {
        this.view = view;
        this.planId = planId;

        mRationPlanDao = DBManager.getInstance().getRationPlanDao();
        mRationRecordDao = DBManager.getInstance().getRationRecordDao();
    }

    @Override
    public void update() {
        Observable.create((ObservableEmitter<Integer> e) -> {
            listData.clear();
            listData.addAll(mRationRecordDao.queryBuilder()
                    .where(RationRecordDao.Properties.ParentPlanId.eq(planId))
                    .list());
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
                    view.notifyDataChanged(listData);
                    if (value == 0) {
                        view.showNoActionSnackbar("计划不存在，刚刚被你删了吧？");
                        mHandler.postDelayed(() -> ((Activity) view).finish(), 700);
                    } else {
                        mHandler.postDelayed(() -> view.fillRationPlanData(rationPlan), 500);
                    }
                });
    }
}
