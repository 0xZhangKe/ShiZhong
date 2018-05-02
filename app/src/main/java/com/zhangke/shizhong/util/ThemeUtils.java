package com.zhangke.shizhong.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.widget.RippleAnimationView;
import com.zhangke.zlog.ZLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 日间/夜间模式切换工具
 * <p>
 * Created by ZhangKe on 2018/5/2.
 */
public class ThemeUtils {

    private static final String TAG = "ThemeUtils";

    /**
     * 刷新UI界面
     */
    public static void refreshUI(Activity activity, ViewGroup rootView) {
        Resources resources = activity.getResources();
        TypedValue toolbarColor = new TypedValue();//Toolbar 背景色
        TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(R.attr.toolbarColor, toolbarColor, true);
        theme.resolveAttribute(R.attr.backgroundColor, background, true);
        theme.resolveAttribute(R.attr.textColor, textColor, true);

        rootView.setBackgroundResource(background.resourceId);
        List<View> childList = new ArrayList<>();
        getChildView(rootView, childList);
        for (View itemView : childList) {
            Object tag = itemView.getTag();
            if (tag != null
                    && tag instanceof String
                    && TextUtils.equals(activity
                    .getResources()
                    .getString(R.string.change_theme_need_refresh_flag), (String) tag)) {
                if (itemView instanceof TextView) {
                    TextView tv = (TextView) itemView;
                    tv.setTextColor(resources.getColor(textColor.resourceId));
                }
                if (itemView instanceof ViewGroup) {
                    itemView.setBackgroundResource(background.resourceId);
                }
                if (itemView instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) itemView;
                    itemView.setBackgroundResource(toolbarColor.resourceId);
                    toolbar.setTitleTextColor(resources.getColor(textColor.resourceId));
                    toolbar.setSubtitleTextColor(resources.getColor(textColor.resourceId));
                }
                if (itemView instanceof RecyclerView) {
                    refreshRecyclerView((RecyclerView) itemView);
                }
                if (itemView instanceof ImageView) {
                    ImageView img = (ImageView) itemView;
                    Drawable drawable = img.getDrawable();
                    img.setImageDrawable(drawable);
                }
            }
        }

        refreshStatusBar(activity);
    }

    /**
     * 遍历出 ViewGroup 中所有的子 View
     */
    private static void getChildView(ViewGroup viewGroup, List<View> childList) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View item = viewGroup.getChildAt(i);
            childList.add(item);
            if (item instanceof ViewGroup) {
                getChildView((ViewGroup) item, childList);
            } else {
                childList.add(item);
            }
        }
    }

    private static void refreshRecyclerView(RecyclerView recyclerView) {
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(recyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            ZLog.e(TAG, "refreshUI", e);
        } catch (ClassNotFoundException e) {
            ZLog.e(TAG, "refreshUI", e);
        } catch (NoSuchMethodException e) {
            ZLog.e(TAG, "refreshUI", e);
        } catch (InvocationTargetException e) {
            ZLog.e(TAG, "refreshUI", e);
        } catch (IllegalAccessException e) {
            ZLog.e(TAG, "refreshUI", e);
        }
    }

    /**
     * 刷新 StatusBar
     */
    private static void refreshStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = activity.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(typedValue.resourceId));
        }
    }

}
