package com.zhangke.shizhong.common;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Server 接口仓库
 * Created by ZhangKe on 2018/4/18.
 */

public interface ApiStores {

    @GET("j/search?cat=1005")
    Call<ResponseBody> getMovieUsers(@Query("q") String q, @Query("start") int start);

    @GET("people/{userId}/collect?sort=time&rating=all&filter=all&mode=grid")
    Call<ResponseBody> getMoviePosters(@Path("userId") String userId, @Query("start") int start);
}
