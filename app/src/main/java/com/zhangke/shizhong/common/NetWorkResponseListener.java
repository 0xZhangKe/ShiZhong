package com.zhangke.shizhong.common;

/**
 * 网络请求响应监听接口
 * <p>
 * Created by ZhangKe on 2018/4/18.
 */

public interface NetWorkResponseListener {

    interface OnSuccessResponse<T> {
        void onSuccess(T response);
    }

    interface OnError {
        void onError(String cause);
    }
}
