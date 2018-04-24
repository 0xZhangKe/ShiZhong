package com.zhangke.shizhong.page.poster.showposter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.ISaveFileEngine;
import com.zhangke.shizhong.util.SaveFileEngine;
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
        showMoviePosterModel = new ShowMusicPosterModel(this, mAlbumBean);
        showMoviePosterModel.getMusicPoster();

        adapter.setOnItemClickListener((View view, int position) -> {

        });
    }

    private void showPosterDialog() {
        if (showPosterDialog == null) {

        }
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
