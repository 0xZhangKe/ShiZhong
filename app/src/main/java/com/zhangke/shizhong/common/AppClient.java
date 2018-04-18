package com.zhangke.shizhong.common;

import com.zhangke.shizhong.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * 网络请求客户端
 * <p>
 * Created by ZhangKe on 2018/4/18.
 */

public class AppClient {

    private static Retrofit retrofit = null;

    public static Retrofit retrofit() {
        if (retrofit == null) {
            synchronized (AppClient.class) {
                if (retrofit == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();

                    builder.connectTimeout(15, TimeUnit.SECONDS);
                    builder.readTimeout(20, TimeUnit.SECONDS);
                    builder.writeTimeout(20, TimeUnit.SECONDS);
                    builder.retryOnConnectionFailure(true);

                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        builder.addInterceptor(loggingInterceptor);
                    }

                    OkHttpClient okHttpClient = builder.build();
                    retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl("https://www.douban.com/")
                            .build();
                }
            }
        }
        return retrofit;
    }
}
