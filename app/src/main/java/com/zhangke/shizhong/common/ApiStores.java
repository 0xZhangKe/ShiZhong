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
    @GET("j/search?q=%s&start=%s&cat=1005")
    Call<ResponseBody> get163MusicUsers(@Header("p") String p, @Header("start") int start);

}
