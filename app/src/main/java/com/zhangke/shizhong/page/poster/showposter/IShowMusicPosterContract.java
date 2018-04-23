package com.zhangke.shizhong.page.poster.showposter;

import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 显示云音乐海报
 *
 * Created by ZhangKe on 2018/4/24.
 */

public interface IShowMusicPosterContract {


    interface View extends IBasePage {
        /**
         * 通知 View 海报数据发生变化
         */
        void notifyDataChanged(List<MusicPosterBean.PlaylistBean.TracksBean> list);
    }

    interface Model {
        /**
         * 根据 UserId 获取海报数据
         */
        void getMusicPoster();
    }
}
