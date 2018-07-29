package com.zhangke.shizhong.contract.plan;

import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 打卡计划详情
 *
 * Created by ZhangKe on 2018/7/29.
 */
public interface IClockPlanDetailContract {

    interface View extends IBasePage{
        void updateClockPlan(ClockPlan clockPlan);
        void notifyRecordChanged(List<ClockRecord> list);
    }

    interface Presenter{
        void clearCache();
        void update(String month);
    }
}
