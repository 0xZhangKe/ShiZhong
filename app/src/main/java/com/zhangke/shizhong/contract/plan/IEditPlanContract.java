package com.zhangke.shizhong.contract.plan;

import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.model.plan.EditPlanDataEntity;
import com.zhangke.shizhong.page.base.IBasePage;

/**
 * 修改计划接口约束
 *
 * Created by ZhangKe on 2018/7/19.
 */
public interface IEditPlanContract {

    interface View extends IBasePage{
        void showRationPlan();
        void showClockPlan();
        void fillRationPlanData(RationPlan plan);
        void fillClockPlanData(ClockPlan plan);

    }

    interface Presenter{
        void initDate(long planId, int planType);
        void updatePlan(EditPlanDataEntity editData);
        void deletePeriod();
        void deletePlan();
        void onDestroy();
    }
}
