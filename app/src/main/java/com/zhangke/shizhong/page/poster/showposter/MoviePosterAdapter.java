package com.zhangke.shizhong.page.poster.showposter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 电影海报展示
 * Created by ZhangKe on 2018/4/22.
 */

public class MoviePosterAdapter extends BaseRecyclerAdapter<MoviePosterAdapter.ViewHolder, MoviePosterBean> {

    public MoviePosterAdapter(Context context, List<MoviePosterBean> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_movie_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        @BindView(R.id.img_view)
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
