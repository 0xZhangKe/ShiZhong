package com.zhangke.shizhong.page.poster.showposter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.widget.CircleImageView;
import com.zhangke.shizhong.widget.MultiItemLayoutManger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 显示云音乐专辑
 * Created by ZhangKe on 2018/4/23.
 */

public class MusicAlbumAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.ViewHolder, MusicAlbumBean.ResultBean.PlaylistsBean> {

    MusicAlbumAdapter(Context context, List<MusicAlbumBean.ResultBean.PlaylistsBean> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MultiItemLayoutManger.BANNER_ITEM_TYPE) {
            return new BannerViewHolder(inflater.inflate(R.layout.adapter_music_album_banner, parent, false));
        } else if (viewType == MultiItemLayoutManger.TITLE_ITEM_TYPE) {
            return new TitleViewHolder(inflater.inflate(R.layout.adapter_music_album_title, parent, false));
        } else {
            return new MenuViewHolder(inflater.inflate(R.layout.adapter_music_album_menu, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        MusicAlbumBean.ResultBean.PlaylistsBean item = listData.get(position);
        if (holder instanceof BannerViewHolder) {
            BannerViewHolder titleViewHolder = (BannerViewHolder) holder;
            titleViewHolder.tvUserName.setText(item.getName());
            titleViewHolder.tvDescription.setText(item.getDescription());
            Glide.with(context)
                    .load(item.getUserIcon())
                    .crossFade()
                    .into(titleViewHolder.imgUserIcon);
            Glide.with(context)
                    .load(item.getCoverImgUrl())
                    .crossFade()
                    .into(titleViewHolder.imgBanner);
        } else if (holder instanceof MenuViewHolder) {
            MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
            Glide.with(context)
                    .load(item.getCoverImgUrl())
                    .crossFade()
                    .into(menuViewHolder.imgAlbumPoster);
            menuViewHolder.tvAlbumName.setText(item.getName());
            menuViewHolder.tvDescription.setText(String.format("歌曲数 %s | 播放数 %s", item.getTrackCount(), item.getPlayCount()));
        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.tvTitle.setText(item.getDescription());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getItemType();
    }

    class BannerViewHolder extends ViewHolder {

        @BindView(R.id.img_banner)
        ImageView imgBanner;
        @BindView(R.id.img_user_icon)
        CircleImageView imgUserIcon;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_description)
        TextView tvDescription;

        BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MenuViewHolder extends ViewHolder {

        @BindView(R.id.img_album_poster)
        ImageView imgAlbumPoster;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_album_name)
        TextView tvAlbumName;

        MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TitleViewHolder extends ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
