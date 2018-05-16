package com.zhangke.shizhong.page.poster;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.poster.IInputNameContract;
import com.zhangke.shizhong.model.poster.UserBean;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.presenter.poster.InputNamePresenter;
import com.zhangke.shizhong.widget.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户名搜索结果界面
 * <p>
 * Created by ZhangKe on 2018/4/20.
 */
public class SearchResultActivity extends BaseActivity implements IInputNameContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshRecyclerView pullRecyclerView;

    /**
     * 0-豆瓣电影海报
     * 1-云音乐封面
     */
    private int type = 0;
    private String name;

    private IInputNameContract.Presenter inputNamePresenter;

    private List<UserBean> userList = new ArrayList<>();
    private UserAdapter userAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        type = intent.getIntExtra(INTENT_ARG_01, type);
        name = intent.getStringExtra(INTENT_ARG_02);
        initToolbar(toolbar, name, true);

        inputNamePresenter = new InputNamePresenter(this, this, type);

        initRecycler();

        inputNamePresenter.searchUserFromName(name);
    }

    private void initRecycler() {
        userAdapter = new UserAdapter(this, userList);
        pullRecyclerView.setAdapter(userAdapter);
        pullRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pullRecyclerView.setOnPullToBottomListener(() -> {
            inputNamePresenter.loadMore();
        });

        userAdapter.setOnItemClickListener((View view, int position) -> {
            checkAndRequestPermission(() -> {
                Intent intent = new Intent(this, type == 0 ? ShowMoviePosterActivity.class : ShowMusicAlbumActivity.class);
                intent.putExtra(INTENT_ARG_01, userList.get(position));
                startActivity(intent);
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        });

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_right_to_left);
        pullRecyclerView.getRecyclerView().setLayoutAnimation(animation);
    }

    @Override
    public void notifyUserListChanged(List<UserBean> list) {
        userList.clear();
        userList.addAll(list);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeLoadMoreView() {
        pullRecyclerView.closeLoading();
    }

}
