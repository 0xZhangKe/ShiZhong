package com.zhangke.shizhong.page.poster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.poster.IShowMoviePosterContract;
import com.zhangke.shizhong.model.poster.GlideLoadBitmap;
import com.zhangke.shizhong.model.poster.ILoadBitmap;
import com.zhangke.shizhong.model.poster.MoviePosterBean;
import com.zhangke.shizhong.model.poster.ShowMoviePosterModel;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.model.poster.UserBean;
import com.zhangke.shizhong.util.ISaveFileEngine;
import com.zhangke.shizhong.util.SaveFileEngine;
import com.zhangke.shizhong.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用于显示豆瓣电影海报
 * Created by ZhangKe on 2018/4/22.
 */

public class ShowMoviePosterActivity extends BaseActivity implements IShowMoviePosterContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<MoviePosterBean> posterList = new ArrayList<>();
    private MoviePosterAdapter adapter;

    private IShowMoviePosterContract.Model showMoviePosterModel;

    private ISaveFileEngine saveFileEngine;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_music_poster;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        fullScreen();

        saveFileEngine = new SaveFileEngine();
        ILoadBitmap loadBitmap = new GlideLoadBitmap(this, saveFileEngine);

        adapter = new MoviePosterAdapter(this, posterList, loadBitmap);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setAdapter(adapter);

        UserBean userBean = (UserBean) getIntent().getSerializableExtra(INTENT_ARG_01);
        showMoviePosterModel = new ShowMoviePosterModel(this, this, userBean.getUserId());
        showMoviePosterModel.getMoviePoster();
    }

    @Override
    public void notifyDataChanged(List<MoviePosterBean> list) {
        posterList.clear();
        posterList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        saveFileEngine.exit();
        super.onDestroy();
    }
}
