package com.zhangke.shizhong.common;

import com.zhangke.shizhong.model.poster.DoubanSearchResultUserBean;
import com.zhangke.shizhong.model.poster.MusicSearchResultUserBean;
import com.zhangke.shizhong.model.poster.MusicAlbumBean;
import com.zhangke.shizhong.model.poster.MusicPosterBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Server 接口仓库
 * Created by ZhangKe on 2018/4/18.
 */
public interface ApiStores {

    /**
     * 搜索豆瓣用户
     *
     * @param q     用户名
     * @param start 页码
     */
    @GET("j/search?cat=1005")
    Observable<DoubanSearchResultUserBean> getMovieUsers(@Query("q") String q, @Query("start") int start);

    /**
     * 根据用户ID搜索标记的电影
     *
     * @param userId 用户ID
     * @param start  页码
     */
    @GET("people/{userId}/collect?sort=time&rating=all&filter=all&mode=grid")
    Observable<String> getMoviePosters(@Path("userId") String userId, @Query("start") int start);

    /**
     * 搜索云音乐用户
     *
     * @param s 用户名
     */
    @GET("cloudmusic/?type=search&search_type=1002")
    Observable<MusicSearchResultUserBean> getMusicUsers(@Query("s") String s);

    /**
     * 获取云音乐用户创建的歌单
     *
     * @param s 用户名
     */
    @GET("cloudmusic/?type=search&search_type=1000")
    Observable<MusicAlbumBean> getAlbumWithUser(@Query("s") String s);

    /**
     * 获取歌单中的歌曲列表
     *
     * @param id 歌单id
     */
    @GET("cloudmusic/?type=playlist")
    Observable<MusicPosterBean> getMusicsWithAlbum(@Query("id") String id);
}
