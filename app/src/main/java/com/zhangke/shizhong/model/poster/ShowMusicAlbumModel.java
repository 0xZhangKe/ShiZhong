package com.zhangke.shizhong.model.poster;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.shizhong.contract.poster.IShowMusicAlbumContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 云音乐专辑Model
 * Created by ZhangKe on 2018/4/23.
 */

public class ShowMusicAlbumModel implements IShowMusicAlbumContract.Model {

    private IShowMusicAlbumContract.View showMusicAlbumView;
    private String userName;

    private List<MusicAlbumBean.ResultBean.PlaylistsBean> listData = new ArrayList<>();

    public ShowMusicAlbumModel(IShowMusicAlbumContract.View showMusicAlbumView, String userName) {
        this.showMusicAlbumView = showMusicAlbumView;
        this.userName = userName;
    }

    @Override
    public void getAlbum() {
        showMusicAlbumView.showRoundProgressDialog();

        ApiStores apiStores = AppClient.musicRetrofit().create(ApiStores.class);
        apiStores.getAlbumWithUser(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicAlbumBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MusicAlbumBean response) {
                        showMusicAlbumView.closeRoundProgressDialog();
                        try {
                            if (response != null) {
                                if (response.getCode() == 200) {
                                    if (response.getResult() != null
                                            && response.getResult().getPlaylists() != null
                                            && !response.getResult().getPlaylists().isEmpty()) {
                                        listData.addAll(response.getResult().getPlaylists());
                                    }
                                    showMusicAlbumView.notifyDataChanged(listData);
                                } else {
                                    showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                                }
                            } else {
                                showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                            }
                        } catch (Exception e) {
                            showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showMusicAlbumView.closeRoundProgressDialog();
                        showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.internet_error));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
