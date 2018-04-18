package com.zhangke.shizhong.common;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Server 接口仓库
 * Created by ZhangKe on 2018/4/18.
 */

public interface ApiStores {

    @Headers("cat: 1005")
    @GET("j/search")
    Call<ResponseBody> get163MusicUsers(@Header("q") String q, @Header("start") int start);

}
