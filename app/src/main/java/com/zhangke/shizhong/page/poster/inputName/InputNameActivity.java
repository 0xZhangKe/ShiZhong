package com.zhangke.shizhong.page.poster.inputName;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.widget.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入名字及展示搜索结果界面
 * Created by ZhangKe on 2018/4/15.
 */

public class InputNameActivity extends BaseActivity implements IInputNameContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.pull_recycler_view)
    PullToRefreshRecyclerView pullRecyclerView;

    /**
     * 0-豆瓣电影海报
     * 1-云音乐封面
     */
    private int type = 0;

    private IInputNameContract.Presenter inputNamePresenter;

    private List<UserBean> userList = new ArrayList<>();
    private UserAdapter userAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.poster_activity_input_name;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_ARG_01)) {
            type = intent.getIntExtra(INTENT_ARG_01, type);
        }

        initToolbar(toolbar, type == 0 ? "豆瓣电影海报" : "云音乐封面", true);

        inputNamePresenter = new InputNamePresenter(this, this, type);


        userAdapter = new UserAdapter(this, userList);
        pullRecyclerView.setAdapter(userAdapter);
        pullRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pullRecyclerView.setOnPullToBottomListener(() -> {
            inputNamePresenter.loadMore();
        });

        userAdapter.setOnItemClickListener((View view, int position) -> {
//            Intent intent = new Intent(this, MovieActivity.class);
//            intent.putExtra(INTENT_ARG_01, listData.get(position).getUserId() + "");
//            startActivity(intent);
        });
    }

    @OnClick(R.id.btn_search)
    public void onViewClick(View view) {
        inputNamePresenter.searchUserFromName(etName.getText().toString());
    }

    @Override
    public void resetView() {
        userList.clear();
        userAdapter.notifyDataSetChanged();
        llSearch.setVisibility(View.VISIBLE);
        pullRecyclerView.setVisibility(View.GONE);
        btnSearch.setText(getString(R.string.search_name_text));
        progressBar.setVisibility(View.GONE);
        etName.setText("");
    }

    @Override
    public void notifyUserListChanged(List<UserBean> list) {
        userList.clear();
        userList.addAll(list);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNameList() {
        llSearch.setVisibility(View.GONE);
        pullRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeNameList() {
        userList.clear();
        userAdapter.notifyDataSetChanged();
        llSearch.setVisibility(View.VISIBLE);
        pullRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setButtonLoading(boolean loading) {
        if (loading) {
            btnSearch.setText("");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnSearch.setText(getString(R.string.search_name_text));
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void closeLoadMoreView() {
        pullRecyclerView.closeLoading();
    }

    @Override
    public void onBackPressed() {
        if (pullRecyclerView.getVisibility() == View.VISIBLE) {
            closeNameList();
        } else {
            super.onBackPressed();
        }
    }
}
