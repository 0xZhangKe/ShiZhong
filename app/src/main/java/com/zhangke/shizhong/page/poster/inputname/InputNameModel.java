package com.zhangke.shizhong.page.poster.inputname;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.NetWorkResponseListener;
import com.zhangke.shizhong.common.SZApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
        ApiStores apiStores = AppClient.doubanRetrofit().create(ApiStores.class);
        apiStores.getMovieUsers(name, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DoubanSearchResultUserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DoubanSearchResultUserBean resultBean) {
                        try {
                            if (resultBean != null) {
                                List<String> userList = resultBean.getItems();
                                if (userList != null && !userList.isEmpty()) {
                                    for (String s : userList) {
                                        listData.add(new UserBean(s));
                                    }
                                    start += 20;
                                }
                                successResponseListener.onSuccess(listData);
                            } else {
                                onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
                            }
                        } catch (Exception e) {
                            onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onErrorListener.onError(SZApplication.getInstance().getString(R.string.internet_error));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void get163MusicUsers(String name,
                                 NetWorkResponseListener.OnSuccessResponse successResponseListener,
                                 NetWorkResponseListener.OnError onErrorListener) {
        ApiStores apiStores = AppClient.musicRetrofit().create(ApiStores.class);
        apiStores.getMusicUsers(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicSearchResultUserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MusicSearchResultUserBean resultBean) {
                        try {
                            if (resultBean != null) {
                                if (resultBean.getCode() == 200) {
                                    List<MusicSearchResultUserBean.ResultBean.UserprofilesBean> userList = resultBean.getResult().getUserprofiles();
                                    if (userList != null && !userList.isEmpty()) {
                                        for (MusicSearchResultUserBean.ResultBean.UserprofilesBean s : userList) {
                                            listData.add(new UserBean(s));
                                        }
                                    }
                                    successResponseListener.onSuccess(listData);
                                } else {
                                    onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
                                }
                            } else {
                                onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
                            }
                        } catch (Exception e) {
                            onErrorListener.onError(SZApplication.getInstance().getString(R.string.data_error));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onErrorListener.onError(SZApplication.getInstance().getString(R.string.internet_error));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
