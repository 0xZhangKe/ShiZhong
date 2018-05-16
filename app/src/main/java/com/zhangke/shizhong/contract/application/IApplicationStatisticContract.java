package com.zhangke.shizhong.contract.application;

import com.zhangke.shizhong.db.ApplicationInfo;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * APP统计
 *
 * Created by ZhangKe on 2018/4/26.
 */

public interface IApplicationStatisticContract {

    interface View extends IBasePage{
        void notifyDataChanged(List<ApplicationInfo> list);
        void showTitleProgress();
        void closeTitleProgress();
    }

    interface Presenter{
        /**
         * 是否显示系统APP
         */
        void getApplication(boolean showSystem);

        void onDestroy();
    }

}
