package com.zhangke.shizhong.page.poster.showposter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用于显示云音乐海报
 * <p>
 * Created by ZhangKe on 2018/4/21.
 */

public class ShowMusicPosterActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_music_poster;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

    }

}
