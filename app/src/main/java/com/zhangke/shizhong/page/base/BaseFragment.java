package com.zhangke.shizhong.page.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.util.UiUtils;
import com.zhangke.shizhong.widget.RoundProgressDialog;

/**
 * Fragment 基类
 * Created by ZhangKe on 2018/4/15.
 */

public abstract class BaseFragment extends Fragment implements IBasePage {

    protected final String TAG = this.getClass().getSimpleName();

    private Snackbar snackbar;

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    private boolean fragmentIsFirstVisible = true;

    private RoundProgressDialog roundProgressDialog;
    protected Activity mActivity;
    protected ViewGroup rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisible();
            isFragmentVisible = true;
            if (fragmentIsFirstVisible) {
                onFragmentFirstVisible();
                fragmentIsFirstVisible = false;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        roundProgressDialog = new RoundProgressDialog(mActivity);
        rootView = (ViewGroup) inflater.inflate(getLayoutResId(), container, false);
        initView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentIsFirstVisible = true;
    }

    /**
     * 实现Fragment数据的缓加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisible();
            isFragmentVisible = true;
            if (fragmentIsFirstVisible) {
                onFragmentFirstVisible();
                fragmentIsFirstVisible = false;
            }
            return;
        }
        if (isFragmentVisible) {
            onFragmentGone();
            isFragmentVisible = false;
        }
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    /**
     * 如果需要用到懒加载，可以使用此方法加载数据
     */
    protected void onFragmentVisible() {
    }

    /**
     * Fragment第一次显示
     */
    protected void onFragmentFirstVisible() {
    }

    /**
     * Fragment不可见时调用此方法
     */
    protected void onFragmentGone() {
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    @Override
    public void showToastMessage(final String msg) {
        if (mActivity == null) return;
        mActivity.runOnUiThread(() -> {
            UiUtils.showToast(mActivity, msg);
        });
    }

    /**
     * 显示圆形加载对话框，默认消息（请稍等...）
     */
    @Override
    public void showRoundProgressDialog() {
        if (mActivity == null) return;
        mActivity.runOnUiThread(() -> {
            if (!roundProgressDialog.isShowing()) {
                roundProgressDialog.showProgressDialog();
            }
        });
    }

    /**
     * 显示圆形加载对话框
     *
     * @param msg 提示消息
     */
    @Override
    public void showRoundProgressDialog(final String msg) {
        if (mActivity == null) return;
        mActivity.runOnUiThread(() -> {
            if (!roundProgressDialog.isShowing()) {
                roundProgressDialog.showProgressDialog(msg);
            }
        });
    }

    /**
     * 关闭圆形加载对话框
     */
    @Override
    public void closeRoundProgressDialog() {
        if (mActivity == null) return;
        mActivity.runOnUiThread(() -> {
            if (roundProgressDialog.isShowing()) {
                roundProgressDialog.closeProgressDialog();
            }
        });
    }

    @Override
    public void showNoActionSnackbar(String msg) {
        snackbar = Snackbar.make(rootView.findViewById(R.id.coordinator), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Nullable
    @Override
    public Context getContext() {
        return mActivity;
    }
}
