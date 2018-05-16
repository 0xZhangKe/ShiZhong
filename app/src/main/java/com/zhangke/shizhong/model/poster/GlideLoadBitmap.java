package com.zhangke.shizhong.model.poster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhangke.shizhong.util.ISaveFileEngine;

import java.io.File;

/**
 * 使用 Glide 下载图片
 * Created by ZhangKe on 2018/4/22.
 */

public class GlideLoadBitmap implements ILoadBitmap {

    private Context mContext;
    private ISaveFileEngine saveFileEngine;

    GlideLoadBitmap(Context context, ISaveFileEngine saveFileEngine) {
        this.mContext = context;
        this.saveFileEngine = saveFileEngine;
    }

    @Override
    public void loadBitmapIntoImageView(final String url,
                                        final ImageView imageView,
                                        final File file) {
        if (file.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        } else {
            SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imageView.setImageBitmap(resource);
                    saveFileEngine.addFile(file, resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    imageView.setVisibility(View.GONE);
                }
            };
            Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(target);
        }
    }
}
