package com.zhangke.shizhong.page.poster.showposter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.SZApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<ResponseBody> call = apiStores.getAlbumWithUser(userName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showMusicAlbumView.closeRoundProgressDialog();
                try {
                    if (response.body() != null) {
                        MusicAlbumBean musicAlbumBean = JSON.parseObject(response.body().string(), new TypeReference<MusicAlbumBean>() {
                        });
                        if (musicAlbumBean.getCode() == 200) {
                            if (musicAlbumBean.getResult() != null
                                    && musicAlbumBean.getResult().getPlaylists() != null
                                    && !musicAlbumBean.getResult().getPlaylists().isEmpty()) {
                                listData.addAll(musicAlbumBean.getResult().getPlaylists());
                            }
                            showMusicAlbumView.notifyDataChanged(listData);
                        } else {
                            showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                        }
                    } else {
                        showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                    }
                } catch (IOException e) {
                    showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showMusicAlbumView.closeRoundProgressDialog();
                showMusicAlbumView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.internet_error));
            }
        });
    }
}
