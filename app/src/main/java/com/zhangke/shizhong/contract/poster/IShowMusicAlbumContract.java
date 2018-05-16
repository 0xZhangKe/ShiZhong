package com.zhangke.shizhong.contract.poster;

import com.zhangke.shizhong.page.base.IBasePage;
import com.zhangke.shizhong.model.poster.MusicAlbumBean;

import java.util.List;

/**
 * 云音乐专辑约束接口
 * Created by ZhangKe on 2018/4/23.
 */

public interface IShowMusicAlbumContract {

    interface View extends IBasePage{
        void notifyDataChanged(List<MusicAlbumBean.ResultBean.PlaylistsBean> list);
    }

    interface Model{
        void getAlbum();
    }
}
