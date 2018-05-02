package com.zhangke.shizhong.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * 切换日间/夜间模式时的动画 View
 * <p>
 * Created by ZhangKe on 2018/5/2.
 */
@SuppressLint("ViewConstructor")
public class RippleAnimationView extends View {

    private Bitmap mBackground;
    private Paint mPaint;
    private int mMaxRadius, mStartRadius, mCurrentRadius;
    private boolean isStarted;
    private long mDuration;
    private float mStartX, mStartY;
    private ViewGroup mRootView;
    private OnAnimationEndListener mOnAnimationEndListener;
    private Animator.AnimatorListener mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    public static RippleAnimationView create(View onClickView) {
        Context context = onClickView.getContext();
        int newWidth = onClickView.getWidth() / 2;
        int newHeight = onClickView.getHeight() / 2;
        float startX = getAbsoluteX(onClickView) + newWidth;
        float startY = getAbsoluteY(onClickView) + newHeight;
        int radius = Math.max(newWidth, newHeight);
        return new RippleAnimationView(context, startX, startY, radius);
    }

    private RippleAnimationView(Context context, float startX, float startY, int radius) {
        super(context);
        mRootView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        mStartX = startX;
        mStartY = startY;
        mStartRadius = radius;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        updateMaxRadius();
        initListener();
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            updateBackground();
            attachToRootView();
            getAnimator().start();
        }
    }

    public RippleAnimationView setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    private void initListener() {
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                detachFromRootView();
                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener.onAnimationEnd();
                }
                isStarted = false;
            }
        };
        mAnimatorUpdateListener = animation -> {
            mCurrentRadius = (int) (float) animation.getAnimatedValue() + mStartRadius;
            postInvalidate();
        };
    }

    private void updateMaxRadius() {
        RectF leftTop = new RectF(0, 0, mStartX + mStartRadius, mStartY + mStartRadius);
        RectF rightTop = new RectF(leftTop.right, 0, mRootView.getRight(), leftTop.bottom);
        RectF leftBottom = new RectF(0, leftTop.bottom, leftTop.right, mRootView.getBottom());
        RectF rightBottom = new RectF(leftBottom.right, leftTop.bottom, mRootView.getRight(), leftBottom.bottom);
        double leftTopHypotenuse = Math.sqrt(Math.pow(leftTop.width(), 2) + Math.pow(leftTop.height(), 2));
        double rightTopHypotenuse = Math.sqrt(Math.pow(rightTop.width(), 2) + Math.pow(rightTop.height(), 2));
        double leftBottomHypotenuse = Math.sqrt(Math.pow(leftBottom.width(), 2) + Math.pow(leftBottom.height(), 2));
        double rightBottomHypotenuse = Math.sqrt(Math.pow(rightBottom.width(), 2) + Math.pow(rightBottom.height(), 2));
        mMaxRadius = (int) Math.max(
                Math.max(leftTopHypotenuse, rightTopHypotenuse),
                Math.max(leftBottomHypotenuse, rightBottomHypotenuse));
    }

    private void attachToRootView() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.addView(this);
    }

    private void detachFromRootView() {
        mRootView.removeView(this);
    }

    private void updateBackground() {
        if (mBackground != null && !mBackground.isRecycled()) {
            mBackground.recycle();
        }
        mRootView.setDrawingCacheEnabled(true);
        mBackground = mRootView.getDrawingCache();
        mBackground = Bitmap.createBitmap(mBackground);
        mRootView.setDrawingCacheEnabled(false);
    }

    private static float getAbsoluteX(View view) {
        float x = view.getX();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            x += getAbsoluteX((View) parent);
        }
        return x;
    }

    private static float getAbsoluteY(View view) {
        float y = view.getY();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            y += getAbsoluteY((View) parent);
        }
        return y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius, mPaint);
        canvas.restoreToCount(layer);
    }

    private ValueAnimator getAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mMaxRadius).setDuration(mDuration);
        valueAnimator.addUpdateListener(mAnimatorUpdateListener);
        valueAnimator.addListener(mAnimatorListener);
        return valueAnimator;
    }

    public RippleAnimationView setOnAnimationEndListener(OnAnimationEndListener listener) {
        mOnAnimationEndListener = listener;
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }
}
