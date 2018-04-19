package com.zhangke.shizhong.page.poster.inputName;

import com.zhangke.shizhong.common.NetWorkResponseListener;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 搜索姓名界面的接口
 * <p>
 * Created by ZhangKe on 2018/4/17.
 */

public interface IInputNameContract {

    interface View extends IBasePage {
        /**
         * 设置 View 到初始状态
         */
        void resetView();

        /**
         * 显示用户列表
         */
        void showNameList();

        /**
         * 关闭用户列表
         */
        void closeNameList();

        /**
         * 设置按钮上的加载框状态
         *
         * @param loading 是否显示加载框
         */
        void setButtonLoading(boolean loading);

        void notifyUserListChanged(List<UserBean> list);

        /**
         * 关闭上拉加载进度框
         */
        void closeLoadMoreView();
    }

    interface Presenter {
        /**
         * 通过姓名搜索用户
         */
        void searchUserFromName(String name);

        /**
         * 加载下一页
         */
        void loadMore();

        void clearUsers();
    }

    interface Model {
        void reset();
        void getDoubanUsers(String name,
                            NetWorkResponseListener.OnSuccessResponse<List<UserBean>> successResponseListener,
                            NetWorkResponseListener.OnError onErrorListener);
        void get163MusicUsers(String name,
                            NetWorkResponseListener.OnSuccessResponse successResponseListener,
                            NetWorkResponseListener.OnError onErrorListener);
    }
}
