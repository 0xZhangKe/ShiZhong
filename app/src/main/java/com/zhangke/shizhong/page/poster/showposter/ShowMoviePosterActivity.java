package com.zhangke.shizhong.page.poster.showposter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用于显示豆瓣电影海报
 * Created by ZhangKe on 2018/4/22.
 */

public class ShowMoviePosterActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String userId;
    private String nickName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_music_poster;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        fullScreen();

        Intent intent = getIntent();
        userId = getIntent().getStringExtra(INTENT_ARG_01);
        if(intent.hasExtra(INTENT_ARG_02)){
            nickName = getIntent().getStringExtra(INTENT_ARG_02);
        }


    }
}
