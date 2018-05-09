package com.zhangke.shizhong.page.plan;

import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 显示计划界面的接口及业务逻辑约束类
 *
 * Created by ZhangKe on 2018/5/9.
 */

public interface IShowPlanContract {

    interface View extends IBasePage{
        void notifyPlanDataChanged(List<Plan> planList);
    }

    interface Presenter{
        void getPlanData();
        void onDestroy();
    }
}
