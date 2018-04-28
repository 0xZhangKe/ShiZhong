package com.zhangke.shizhong.page.poster.showposter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.ISaveFileEngine;
import com.zhangke.shizhong.util.SaveFileEngine;
import com.zhangke.shizhong.util.UiUtils;
import com.zhangke.shizhong.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用于显示云音乐海报
 * <p>
 * Created by ZhangKe on 2018/4/21.
 */

public class ShowMusicPosterActivity extends BaseActivity implements IShowMusicPosterContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<MusicPosterBean.PlaylistBean.TracksBean> posterList = new ArrayList<>();
    private MusicPosterAdapter adapter;

    private IShowMusicPosterContract.Model showMoviePosterModel;

    private MusicAlbumBean.ResultBean.PlaylistsBean mAlbumBean;

    private AlertDialog showPosterDialog;

    @Override
    protected int getLayoutResId() {
        initTheme();
        return R.layout.activity_show_music_poster;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        fullScreen();

        adapter = new MusicPosterAdapter(this, posterList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setAdapter(adapter);

        mAlbumBean = (MusicAlbumBean.ResultBean.PlaylistsBean) getIntent().getSerializableExtra(INTENT_ARG_01);
        showMoviePosterModel = new ShowMusicPosterModel(this, this, mAlbumBean);
        showMoviePosterModel.getMusicPoster();

        adapter.setOnItemClickListener((View view, int position) -> {
            showPosterDialog(posterList.get(position));
        });
    }

    private void initTheme() {
        if (APPConfig.getTheme() == 0) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
    }

    private void showPosterDialog(final MusicPosterBean.PlaylistBean.TracksBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TranslucentDialog);

        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_show_music_poster, null);

        PhotoView photoView = rootView.findViewById(R.id.photo_view);
        setupPhotoView(photoView);

        TextView tvMusicName = rootView.findViewById(R.id.tv_music_name);
        rootView.findViewById(R.id.img_download).setOnClickListener(v -> {
            showMoviePosterModel.downloadImage(bean);
        });
        tvMusicName.setText(bean.getName());
        Glide.with(ShowMusicPosterActivity.this)
                .load(bean.getAl().getPicUrl())
                .crossFade()
                .into(photoView);
        builder.setView(rootView);
        showPosterDialog = builder.create();
        showPosterDialog.show();

        Window window = showPosterDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.MusicPosterDialogAnim);
        }
    }

    private void setupPhotoView(PhotoView photoView) {
        ViewGroup.LayoutParams layoutParams = photoView.getLayoutParams();
        layoutParams.width = (int) (UiUtils.getScreenWidth(this) * 0.8);
        layoutParams.height = layoutParams.width;
        photoView.setLayoutParams(layoutParams);
    }

    @Override
    public void notifyDataChanged(List<MusicPosterBean.PlaylistBean.TracksBean> list) {
        posterList.clear();
        posterList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
