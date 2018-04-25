package com.zhangke.shizhong.page.poster.showposter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.shizhong.util.FileUtils;
import com.zhangke.shizhong.util.PosterUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用于显示云音乐海报
 * <p>
 * Created by ZhangKe on 2018/4/24.
 */

public class ShowMusicPosterModel implements IShowMusicPosterContract.Model {

    private Context context;
    private IShowMusicPosterContract.View showMusicPosterView;
    private MusicAlbumBean.ResultBean.PlaylistsBean mAlbumBean;

    private List<MusicPosterBean.PlaylistBean.TracksBean> listData = new ArrayList<>();

    ShowMusicPosterModel(Context context,
                         IShowMusicPosterContract.View showMusicPosterView,
                         MusicAlbumBean.ResultBean.PlaylistsBean mAlbumBean) {
        this.context = context;
        this.showMusicPosterView = showMusicPosterView;
        this.mAlbumBean = mAlbumBean;
    }

    @Override
    public void getMusicPoster() {
        showMusicPosterView.showRoundProgressDialog();
        ApiStores apiStores = AppClient.musicRetrofit().create(ApiStores.class);
        Call<ResponseBody> call = apiStores.getMusicsWithAlbum(mAlbumBean.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showMusicPosterView.closeRoundProgressDialog();
                try {
                    if (response.body() != null) {
                        MusicPosterBean musicAlbumBean = JSON.parseObject(response.body().string(), new TypeReference<MusicPosterBean>() {
                        });
                        if (musicAlbumBean.getCode() == 200) {
                            if (musicAlbumBean.getPlaylist() != null
                                    && musicAlbumBean.getPlaylist() != null
                                    && musicAlbumBean.getPlaylist().getTracks() != null) {
                                listData.addAll(musicAlbumBean.getPlaylist().getTracks());
                            }
                            showMusicPosterView.notifyDataChanged(listData);
                        } else {
                            showMusicPosterView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.internet_error));
                        }
                    } else {
                        showMusicPosterView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                    }
                } catch (IOException e) {
                    showMusicPosterView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showMusicPosterView.closeRoundProgressDialog();
                showMusicPosterView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.internet_error));
            }
        });
    }

    @Override
    public void downloadImage(MusicPosterBean.PlaylistBean.TracksBean bean) {
        File file = PosterUtils.getMusicFileWithName(mAlbumBean.getName(), bean.getName(), bean.getAl().getName());
        if (file.exists()) {
            file.delete();
        }
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (FileUtils.saveBitmapToDisk(file, resource)) {
                    showMusicPosterView.showNoActionSnackbar("已下载至:" + file.getPath());
                } else {
                    showMusicPosterView.showNoActionSnackbar("下失败");
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                showMusicPosterView.showNoActionSnackbar("下失败");
            }
        };
        Glide.with(context)
                .load(bean.getAl().getPicUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(target);
    }
}
