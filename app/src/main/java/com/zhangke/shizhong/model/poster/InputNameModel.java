package com.zhangke.shizhong.model.poster;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.NetWorkResponseListener;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.shizhong.contract.poster.IInputNameContract;
import com.zhangke.shizhong.util.HttpObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 输入用户面界面的 model 层
 * Created by ZhangKe on 2018/4/18.
 */

public class InputNameModel implements IInputNameContract.Model {

    private static final String TAG = "InputNameModel";

    private int start = 0;
    private List<UserBean> listData = new ArrayList<>();
    private ApiStores apiStores = AppClient.doubanRetrofit().create(ApiStores.class);

    public InputNameModel() {
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
        apiStores.getMovieUsers(name, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpObserver<DoubanSearchResultUserBean>() {
                    @Override
                    protected void onErrorResponse(String errorMessage) {
                        onErrorListener.onError(errorMessage);
                    }

                    @Override
                    protected void onSuccessResponse(DoubanSearchResultUserBean response) {
                        try {
                            if (response != null) {
                                List<String> userList = response.getItems();
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
                });
    }

    @Override
    public void get163MusicUsers(String name,
                                 NetWorkResponseListener.OnSuccessResponse<List<UserBean>> successResponseListener,
                                 NetWorkResponseListener.OnError onErrorListener) {
        apiStores.getMusicUsers(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpObserver<MusicSearchResultUserBean>() {
                    @Override
                    protected void onErrorResponse(String errorMessage) {
                        onErrorListener.onError(errorMessage);
                    }

                    @Override
                    protected void onSuccessResponse(MusicSearchResultUserBean response) {
                        try {
                            if (response != null) {
                                if (response.getCode() == 200) {
                                    List<MusicSearchResultUserBean.ResultBean.UserprofilesBean> userList = response.getResult().getUserprofiles();
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
                });
    }
}
