package com.zhangke.shizhong.page.poster.showposter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.FileUtils;
import com.zhangke.shizhong.util.PosterUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 电影海报展示
 * Created by ZhangKe on 2018/4/22.
 */

public class MoviePosterAdapter extends BaseRecyclerAdapter<MoviePosterAdapter.ViewHolder, MoviePosterBean> {

    private ILoadBitmap loadBitmap;

    public MoviePosterAdapter(Context context, List<MoviePosterBean> listData) {
        super(context, listData);

        loadBitmap = new GlideLoadBitmap(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_movie_poster, parent, false));
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
