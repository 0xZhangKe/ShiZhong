package com.zhangke.shizhong.model.poster;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.shizhong.contract.poster.IShowMusicAlbumContract;
import com.zhangke.shizhong.util.HttpObserver;

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
    private ApiStores apiStores = AppClient.musicRetrofit().create(ApiStores.class);

    public ShowMusicAlbumModel(IShowMusicAlbumContract.View showMusicAlbumView, String userName) {
        this.showMusicAlbumView = showMusicAlbumView;
        this.userName = userName;
    }

    @Override
    public void getAlbum() {
        showMusicAlbumView.showRoundProgressDialog();
        apiStores.getAlbumWithUser(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpObserver<MusicAlbumBean>() {
                    @Override
                    protected void onErrorResponse(String errorMessage) {
                        showMusicAlbumView.closeRoundProgressDialog();
                        showMusicAlbumView.showNoActionSnackbar(errorMessage);
                    }

                    @Override
                    protected void onSuccessResponse(MusicAlbumBean response) {
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
                });
    }
}
