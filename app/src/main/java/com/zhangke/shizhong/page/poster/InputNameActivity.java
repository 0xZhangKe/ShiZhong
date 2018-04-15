package com.zhangke.shizhong.page.poster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入名字及展示搜索结果界面
 * Created by ZhangKe on 2018/4/15.
 */

public class InputNameActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    /**
     * 0-豆瓣电影海报
     * 1-云音乐封面
     */
    private int type = 0;

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

    }

    @OnClick(R.id.btn_search)
    public void onViewClick(View view) {
        showButtonLoading();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    closeButtonLoading();
                });
            }
        }, 2000);
    }

    private void showButtonLoading() {
        btnSearch.setText("");
        progressBar.setVisibility(View.VISIBLE);
    }

    private void closeButtonLoading() {
        btnSearch.setText("搜寻");
        progressBar.setVisibility(View.GONE);
    }

}
