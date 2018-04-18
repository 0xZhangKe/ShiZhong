package com.zhangke.shizhong.page.poster.inputName;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.NetWorkResponseListener;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.shizhong.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 输入用户面界面的 model 层
 * Created by ZhangKe on 2018/4/18.
 */

public class InputNameModel implements IInputNameContract.Model {

    private static final String TAG = "InputNameModel";

    private int start = 0;
    private List<UserBean> listData = new ArrayList<>();

    InputNameModel() {
    }

    @Override
    public void reset() {
        start = 0;
        listData.clear();
    }

    @Override
    public void getDoubanUsers(String name,
                               NetWorkResponseListener.OnSuccessResponse<List<UserBean>> successResponseListener,
                               NetWorkResponseListener.OnError onErrorListener) {
//        ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
//        Call<ResponseBody> call = apiStores.get163MusicUsers(HttpUtils.encode(name), start);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if (response.body() != null) {
//                        DoubanSearchResultUserBean resultBean = JSON.parseObject(response.body().string(), new TypeReference<DoubanSearchResultUserBean>() {
//                        });
//                        List<String> userList = resultBean.getItems();
//                        if (userList != null && !userList.isEmpty()) {
//                            for (String s : userList) {
//                                listData.add(new UserBean(s));
//                            }
//                            start += 20;
//                        }
//                        successResponseListener.onSuccess(listData);
//                    } else {
//                        onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
//                    }
//                } catch (IOException e) {
//                    onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                onErrorListener.onError(SZApplication.getInstance().getString(R.string.internet_error));
//            }
//        });
        String url = String.format("https://www.douban.com/j/search?q=%s&start=%s&cat=1005", name, start);

        simpleGetRequest(Volley.newRequestQueue(SZApplication.getInstance()),
                url,
                new OnStringDataCallbackListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, response);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, error);
                    }
                });
    }

    public static void simpleGetRequest(RequestQueue requestQueue,
                                        String url,
                                        final OnStringDataCallbackListener callbackListener) {
        StringRequest request = new StringRequest(url,
                callbackListener::onSuccess,
                (VolleyError volleyError) -> {
                    if (volleyError instanceof TimeoutError) {
                        callbackListener.onError("连接超时");
                    } else {
                        if (null != volleyError.networkResponse) {
                            callbackListener.onError("网络错误");
                        } else {
                            callbackListener.onError("网络错误");
                        }
                    }
                    String message = volleyError.getMessage();
                    callbackListener.onError(TextUtils.isEmpty(message) ? "网络错误" : message);
                });
        requestQueue.add(request);
    }

    interface OnStringDataCallbackListener{
        void onSuccess(String response);
        void onError(String error);
    }


    @Override
    public void get163MusicUsers(String name, NetWorkResponseListener.OnSuccessResponse successResponseListener, NetWorkResponseListener.OnError onErrorListener) {

    }
}
