package com.zhangke.shizhong.contract.plan;

import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 定量计划详情
 *
 * Created by ZhangKe on 2018/7/26.
 */
public interface IRationPlanDetailContract {

    interface View extends IBasePage{
        void fillRationPlanData(RationPlan rationPlan);
        void notifyDataChanged(List<RationRecord> list);
    }

    interface Presenter{
        void update();
    }
}
