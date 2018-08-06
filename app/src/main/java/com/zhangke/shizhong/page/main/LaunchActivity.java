package com.zhangke.shizhong.page.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动页面
 * <p>
 * Created by ZhangKe on 2018/7/29.
 */
public class LaunchActivity extends BaseActivity {

    @BindView(R.id.img_slogan_02)
    ImageView imgSlogan02;
    @BindView(R.id.img_slogan_01)
    ImageView imgSlogan01;

    @Override
    protected int getLayoutResId() {
        fullScreen();
        return R.layout.activity_launch;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        final TranslateAnimation animation_01 = new TranslateAnimation(0, 0, 0, UiUtils.dip2px(this, 40));
        final TranslateAnimation animation_02 = new TranslateAnimation(0, 0, 0, -UiUtils.dip2px(this, 40));
        setupAnim(animation_01);
        setupAnim(animation_02);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()->{
            imgSlogan01.setVisibility(View.VISIBLE);
            imgSlogan01.startAnimation(animation_01);
            handler.postDelayed(()->{
                imgSlogan02.setVisibility(View.VISIBLE);
                imgSlogan02.startAnimation(animation_02);
                handler.postDelayed(()->{
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }, 1000);
            }, 400);
        }, 300);
    }

    private void setupAnim(TranslateAnimation anim){
        anim.setDuration(400);
        anim.setFillAfter(true);
        anim.setRepeatCount(0);
        anim.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected boolean showImmersionWindowStatus() {
        return false;
    }
}
