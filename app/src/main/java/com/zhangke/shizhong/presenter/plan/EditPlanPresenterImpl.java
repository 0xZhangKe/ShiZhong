package com.zhangke.shizhong.presenter.plan;

import android.content.Context;

import com.zhangke.shizhong.contract.plan.IEditPlanContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockPlanDao;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationPlanDao;
import com.zhangke.shizhong.model.plan.EditPlanDataEntity;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private int planType;

    private RationPlanDao mRationPlanDao;
    private ClockPlanDao mClockPlanDao;

    public EditPlanPresenterImpl(Context context, IEditPlanContract.View view) {
        this.context = context;
        this.view = view;

        mRationPlanDao = DBManager.getInstance().getRationPlanDao();
        mClockPlanDao = DBManager.getInstance().getClockPlanDao();
    }

    private void initObservable() {
    }

    @Override
    public void initDate(long planId, int planType) {
        this.planId = planId;
        this.planType = planType;
        if (planType == 0) {
            view.showRationPlan();
            Observable.create((ObservableEmitter<RationPlan> e) -> {
                List<RationPlan> rationPlanList = mRationPlanDao.queryBuilder()
                        .where(RationPlanDao.Properties.Id.eq(planId))
                        .list();
                if(rationPlanList.isEmpty()){
                    e.onNext(null);
                }else{
                    e.onNext(rationPlanList.get(0));
                }
                e.onComplete();
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        } else {
            view.showClockPlan();
        }

    }

    @Override
    public void updatePlan(EditPlanDataEntity editData) {

    }
}
