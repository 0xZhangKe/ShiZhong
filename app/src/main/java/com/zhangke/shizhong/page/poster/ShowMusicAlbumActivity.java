package com.zhangke.shizhong.page.poster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.poster.IShowMusicAlbumContract;
import com.zhangke.shizhong.model.poster.MusicAlbumBean;
import com.zhangke.shizhong.model.poster.ShowMusicAlbumModel;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.model.poster.UserBean;
import com.zhangke.shizhong.widget.MultiItemLayoutManger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 显示云音乐专辑
 * <p>
 * Created by ZhangKe on 2018/4/23.
 */

public class ShowMusicAlbumActivity extends BaseActivity implements IShowMusicAlbumContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private IShowMusicAlbumContract.Model showMusicAlbumModel;

    private List<MusicAlbumBean.ResultBean.PlaylistsBean> listData = new ArrayList<>();
    private MusicAlbumAdapter adapter;

    private UserBean mUserBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_music_album;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        fullScreen();

        findViewById(R.id.img_back).setOnClickListener(view -> finish());

        mUserBean = (UserBean) getIntent().getSerializableExtra(INTENT_ARG_01);

        initRecycler();

        showMusicAlbumModel = new ShowMusicAlbumModel(this, mUserBean.getNickName());
        showMusicAlbumModel.getAlbum();
    }

    private void initRecycler() {
        addTitle();
        adapter = new MusicAlbumAdapter(this, listData);
        recyclerView.setLayoutManager(new MultiItemLayoutManger(2));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            MusicAlbumBean.ResultBean.PlaylistsBean item = listData.get(position);
            if (item.getItemType() == MultiItemLayoutManger.MENU_ITEM_TYPE) {
                Intent intent = new Intent(ShowMusicAlbumActivity.this, ShowMusicPosterActivity.class);
                intent.putExtra(INTENT_ARG_01, listData.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void notifyDataChanged(List<MusicAlbumBean.ResultBean.PlaylistsBean> list) {
        listData.clear();
        addTitle();
        listData.addAll(list);

        MusicAlbumBean.ResultBean.PlaylistsBean titleBean = new MusicAlbumBean.ResultBean.PlaylistsBean();
        titleBean.setDescription(String.format("TA 创建的专辑（%s）", listData.size() - 1));
        titleBean.setItemType(MultiItemLayoutManger.TITLE_ITEM_TYPE);
        listData.add(1, titleBean);

        adapter.notifyDataSetChanged();
    }

    private void addTitle() {
        MusicAlbumBean.ResultBean.PlaylistsBean bannerBean = new MusicAlbumBean.ResultBean.PlaylistsBean();
        bannerBean.setName(mUserBean.getNickName());
        bannerBean.setDescription(String.format("关注 %s  |  粉丝 %s", mUserBean.getFolloweds(), mUserBean.getFollows()));
        bannerBean.setUserIcon(mUserBean.getUserIcon());
        bannerBean.setCoverImgUrl(mUserBean.getBackgroundUrl());
        bannerBean.setItemType(MultiItemLayoutManger.BANNER_ITEM_TYPE);
        listData.add(bannerBean);
    }
}
