package com.zhangke.shizhong.page.poster.showposter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.PosterUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 云音乐海报
 * Created by ZhangKe on 2018/4/24.
 */

public class MusicPosterAdapter  extends BaseRecyclerAdapter<MusicPosterAdapter.ViewHolder, MusicPosterBean.PlaylistBean.TracksBean> {

    private static final String TAG = "MusicPosterAdapter";

    private ILoadBitmap loadBitmap;

    MusicPosterAdapter(Context context,
                       List<MusicPosterBean.PlaylistBean.TracksBean> listData,
                       ILoadBitmap loadBitmap) {
        super(context, listData);
        this.loadBitmap = loadBitmap;
    }

    @NonNull
    @Override
    public MusicPosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicPosterAdapter.ViewHolder(inflater.inflate(R.layout.adapter_movie_poster, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicPosterAdapter.ViewHolder holder, int position) {
        MusicPosterBean.PlaylistBean.TracksBean item = listData.get(position);
        if(item.getAl() != null) {
            loadBitmap.loadBitmapIntoImageView(
                    item.getAl().getPicUrl(),
                    holder.imgView,
                    PosterUtils.getMusicFileWithName(item.getName(), item.getAl().getName()));
        }else{
            holder.imgView.setVisibility(View.GONE);
        }
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
