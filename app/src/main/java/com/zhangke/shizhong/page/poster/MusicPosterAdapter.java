package com.zhangke.shizhong.page.poster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhangke.shizhong.model.poster.MusicPosterBean;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.UiUtils;

import java.util.List;

/**
 * 云音乐海报
 * Created by ZhangKe on 2018/4/24.
 */

public class MusicPosterAdapter extends BaseRecyclerAdapter<MusicPosterAdapter.ViewHolder, MusicPosterBean.PlaylistBean.TracksBean> {

    private static final String TAG = "MusicPosterAdapter";

    private int itemViewHeight = 0;

    MusicPosterAdapter(Context context,
                       List<MusicPosterBean.PlaylistBean.TracksBean> listData) {
        super(context, listData);

        itemViewHeight = UiUtils.getScreenWidth(context) / 4;
    }

    int createCount = 0;

    @NonNull
    @Override
    public MusicPosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, String.format("onCreateViewHolder: 第%s次Create", ++createCount));
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = itemViewHeight;
        imageView.setLayoutParams(layoutParams);
        return new MusicPosterAdapter.ViewHolder(imageView);
    }

    int bindCount = 0;

    @Override
    public void onBindViewHolder(@NonNull MusicPosterAdapter.ViewHolder holder, int position) {
        MusicPosterBean.PlaylistBean.TracksBean item = listData.get(position);
        if (item.getAl() != null) {
            holder.imgView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(item.getAl().getPicUrl())
                    .crossFade()
                    .into(holder.imgView);
        }else{
            holder.imgView.setVisibility(View.GONE);
        }

        Log.d(TAG, String.format("onBindViewHolder: 第%s次Bind", ++bindCount));
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        //        @BindView(R.id.img_view)
        ImageView imgView;

        ViewHolder(ImageView itemView) {
            super(itemView);
            this.imgView = itemView;
//            ButterKnife.bind(this, itemView);
        }
    }
}
