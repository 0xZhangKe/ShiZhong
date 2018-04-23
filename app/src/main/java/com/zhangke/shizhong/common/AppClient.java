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

    private static Retrofit doubanRetrofit = null;
    private static Retrofit moviePosterRetrofit = null;
    private static Retrofit musicRetrofit = null;

    public static Retrofit doubanRetrofit() {
        if (doubanRetrofit == null) {
            synchronized (AppClient.class) {
                if (doubanRetrofit == null) {
                    doubanRetrofit = new Retrofit.Builder()
                            .client(getHttpClient())
                            .baseUrl("https://www.douban.com/")
                            .build();
                }
            }
        }
        return doubanRetrofit;
    }

    public static Retrofit moviePosterRetrofit() {
        if (moviePosterRetrofit == null) {
            synchronized (AppClient.class) {
                if (moviePosterRetrofit == null) {
                    moviePosterRetrofit = new Retrofit.Builder()
                            .client(getHttpClient())
                            .baseUrl("https://movie.douban.com/")
                            .build();
                }
            }
        }
        return moviePosterRetrofit;
    }

    public static Retrofit musicRetrofit() {
        if (musicRetrofit == null) {
            synchronized (AppClient.class) {
                if (musicRetrofit == null) {
                    musicRetrofit = new Retrofit.Builder()
                            .client(getHttpClient())
                            .baseUrl("https://api.imjad.cn/")
                            .build();
                }
            }
        }
        return musicRetrofit;
    }

    private static OkHttpClient getHttpClient() {
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

        return builder.build();
    }
}
