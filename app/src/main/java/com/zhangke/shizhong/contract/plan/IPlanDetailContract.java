package com.zhangke.shizhong.contract.plan;

import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.page.base.IBasePage;

/**
 * 计划详情
 * <p>
 * Created by ZhangKe on 2018/7/22.
 */
public interface IPlanDetailContract {

    interface View extends IBasePage{
        void showRationPlan();
        void showClockPlan();
        void fillRationPlanData(RationPlan plan);
        void fillClockPlanData(ClockPlan plan);
    }

    interface Presenter {
        void initDate(long planId, int planType);
        void onDestroy();
    }
}
