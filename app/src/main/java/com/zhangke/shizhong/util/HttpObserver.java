package com.zhangke.shizhong.util;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonParseException;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.SZApplication;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by ZhangKe on 2018/5/21.
 */
public abstract class HttpObserver<T> implements Observer<T> {

    private static final String TAG = "HttpObserver";

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {
        onSuccessResponse(value);
    }

    @Override
    public void onError(Throwable e) {
        String errorMessage;
        if (e instanceof HttpException) {
            errorMessage = SZApplication.getInstance().getResources().getString(R.string.network_error);
            String detail = getErrorDetail((HttpException) e);
            if (!TextUtils.isEmpty(detail)) {
                errorMessage = detail;
            }
            Log.e(TAG, "网络错误，code：" + ((HttpException) e).code(), e);
        } else if (e instanceof SocketTimeoutException) {
            errorMessage = SZApplication.getInstance().getResources().getString(R.string.time_out_error);
            Log.e(TAG, "连接超时", e);
        } else if (e instanceof ConnectException) {
            errorMessage = SZApplication.getInstance().getResources().getString(R.string.network_error);
            Log.e(TAG, "网络错误", e);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            errorMessage = SZApplication.getInstance().getResources().getString(R.string.data_error);
            Log.e(TAG, "数据错误", e);
        } else {
            errorMessage = SZApplication.getInstance().getResources().getString(R.string.network_error);
            Log.e(TAG, "网络错误", e);
        }
        onErrorResponse(errorMessage);
    }

    @Override
    public void onComplete() {

    }

    private String getErrorDetail(HttpException httpException) {
        String detail = "";
        if (httpException.response() != null) {
            ResponseBody responseErrorBody = httpException.response().errorBody();
            if (responseErrorBody != null) {
                try {
                    String response = responseErrorBody.string();
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject responseJson = JSON.parseObject(response);
                        detail = responseJson.getString("detail");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "response.body() 数据转换 JSON 失败", e);
                }
            }
        }
        return detail;
    }

    protected abstract void onErrorResponse(String errorMessage);

    protected abstract void onSuccessResponse(T response);
}
