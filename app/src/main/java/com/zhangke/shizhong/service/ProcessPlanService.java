package com.zhangke.shizhong.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationPlanDao;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.presenter.plan.PlanHelper;
import com.zhangke.shizhong.util.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 计算计划数据
 * <p>
 * Created by ZhangKe on 2018/8/10.
 */
public class ProcessPlanService extends IntentService {

    private RationPlanDao rationPlanDao;
    private String curDate = DateUtils.getCurrentDate("yyyy-MM-dd");
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ProcessPlanService() {
        super("ProcessPlanService");
        rationPlanDao = DBManager.getInstance().getRationPlanDao();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<RationPlan> rationPlanList = rationPlanDao.loadAll();
        if (rationPlanList != null && !rationPlanList.isEmpty()) {
            for (RationPlan plan : rationPlanList) {
                processRationPlan(plan);
            }
        }
        DBManager.getInstance().clear();
        EventBus.getDefault().post(new PlanChangedEvent());
    }

    private void processRationPlan(RationPlan plan) {
        if (plan.getPeriodIsOpen() &&
                (TextUtils.isEmpty(plan.getLastUpdatePeriodDate()) ||
                        !PlanHelper.isCurPeriod(plan.getPeriodPlanType(), plan.getLastUpdatePeriodDate(), "yyyy-MM-dd"))) {
            //短期计划存在且短期计划上次更新时间为空或者上次更新时间不在当前周期
            plan.setPeriodPlanTarget(Double.valueOf(decimalFormat.format(PlanHelper.getPeriodTarget(plan, plan.getPeriodPlanType()))));
            plan.setLastUpdatePeriodDate(curDate);
            rationPlanDao.update(plan);
        }
    }
}
