package com.zhangke.shizhong.page.poster;

/**
 * 搜索姓名界面的接口
 * <p>
 * Created by ZhangKe on 2018/4/17.
 */

public interface IInputNameContract {

    interface View {
        /**
         * 设置 View 到初始状态
         */
        void resetView();

        /**
         * 显示搜索到的姓名结果
         */
        void showNameList();

        /**
         * 设置按钮上的加载框状态
         *
         * @param loading 是否显示加载框
         */
        void setButtonLoading(boolean loading);
    }

    interface Presenter {
        /**
         * 通过姓名搜索用户
         */
        void searchUserFromName(String name);
    }

    interface Model {

    }
}
