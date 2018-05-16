package com.zhangke.shizhong.page.poster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.model.poster.ILoadBitmap;
import com.zhangke.shizhong.model.poster.MoviePosterBean;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.PosterUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 电影海报展示
 * Created by ZhangKe on 2018/4/22.
 */

public class MoviePosterAdapter extends BaseRecyclerAdapter<MoviePosterAdapter.ViewHolder, MoviePosterBean> {

    private static final String TAG = "MoviePosterAdapter";

    private ILoadBitmap loadBitmap;

    MoviePosterAdapter(Context context,
                       List<MoviePosterBean> listData,
                       ILoadBitmap loadBitmap) {
        super(context, listData);
        this.loadBitmap = loadBitmap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_movie_poster, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        loadBitmap.loadBitmapIntoImageView(
                listData.get(position).getMovieImageUrl(),
                holder.imgView,
                PosterUtils.getMoviePosterFileWithName(listData.get(position).getMovieName()));
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        @BindView(R.id.img_view)
        ImageView imgView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
