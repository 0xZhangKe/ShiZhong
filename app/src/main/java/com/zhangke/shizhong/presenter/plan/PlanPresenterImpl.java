package com.zhangke.shizhong.presenter.plan;

import com.zhangke.shizhong.contract.plan.IShowPlanContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.db.PlanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示计划界面的业务逻辑及数据层
 * Created by ZhangKe on 2018/5/9.
 */

public class PlanPresenterImpl implements IShowPlanContract.Presenter {

    private IShowPlanContract.View mShowPlanView;

    private PlanDao mPlanDao;
    private List<Plan> mPlanList = new ArrayList<>();

    public PlanPresenterImpl(IShowPlanContract.View mShowPlanView) {
        this.mShowPlanView = mShowPlanView;

        mPlanDao = DBManager.getInstance().getPlanDao();
    }

    @Override
    public void getPlanData() {
        mShowPlanView.showRoundProgressDialog();
        mPlanList.clear();//todo 使用 RXAndroid 实现
        mPlanList.addAll(mPlanDao.loadAll());
        mShowPlanView.notifyPlanDataChanged(mPlanList);
        mShowPlanView.closeRoundProgressDialog();
    }

    @Override
    public void onDestroy() {
        mShowPlanView = null;
        mPlanDao = null;
        mPlanList.clear();
        mPlanList = null;
    }
}
