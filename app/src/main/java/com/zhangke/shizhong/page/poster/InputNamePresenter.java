package com.zhangke.shizhong.page.poster;

import android.content.Context;
import android.util.Log;

import com.zhangke.shizhong.common.ApiStores;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 搜索姓名界面Presenter实现类
 *
 * Created by ZhangKe on 2018/4/17.
 */

public class InputNamePresenter implements IInputNameContract.Presenter {

    private static final String TAG = "InputNamePresenter";

    private Context context;
    private IInputNameContract.View inputNameView;
    private int type;

    public InputNamePresenter(Context context, IInputNameContract.View inputNameView, int type) {
        this.context = context;
        this.inputNameView = inputNameView;
        this.type = type;
    }

    @Override
    public void searchUserFromName(String name) {
        if(type == 0){
            get163MusicUsers(name);
        }
    }

    private void get163MusicUsers(String name){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.douban.com/")
                .build();
        ApiStores apiStores = retrofit.create(ApiStores.class);
        Call<ResponseBody> call = apiStores.get163MusicUsers(name, 1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "response=" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure=" + t.getMessage());
            }
        });

    }
}
