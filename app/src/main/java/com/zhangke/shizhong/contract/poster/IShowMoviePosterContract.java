package com.zhangke.shizhong.contract.poster;

import com.zhangke.shizhong.page.base.IBasePage;
import com.zhangke.shizhong.model.poster.MoviePosterBean;

import java.util.List;

/**
 * 电影海报模块约束类
 * <p>
 * Created by ZhangKe on 2018/4/22.
 */

public interface IShowMoviePosterContract {

    interface View extends IBasePage {
        /**
         * 通知 View 海报数据发生变化
         */
        void notifyDataChanged(List<MoviePosterBean> list);
    }

    interface Model {
        /**
         * 根据 UserId 获取海报数据
         */
        void getMoviePoster();
    }
}
