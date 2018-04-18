package com.zhangke.shizhong.page.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.util.UiUtils;
import com.zhangke.shizhong.widget.RoundProgressDialog;

/**
 * Activity 基类
 * Created by ZhangKe on 2018/4/15.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBasePage {

    protected final String TAG = this.getClass().getSimpleName();

    private Snackbar snackbar;

    private RoundProgressDialog roundProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        roundProgressDialog = new RoundProgressDialog(this);
        initView(savedInstanceState);
    }

    protected abstract int getLayoutResId();

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 全屏，隐藏状态栏
     */
    protected void fullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void initToolbar(Toolbar toolbar, String title, boolean showBackBtn) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackBtn);
        if (showBackBtn) {
            toolbar.setNavigationOnClickListener((View v) -> {
                onBackPressed();
            });
        }
    }

    @Override
    public void showToastMessage(final String msg) {
        runOnUiThread(() -> {
            UiUtils.showToast(BaseActivity.this, msg);
        });
    }

    /**
     * 显示圆形加载对话框，默认消息（请稍等...）
     */
    @Override
    public void showRoundProgressDialog() {
        runOnUiThread(() -> {
            if (roundProgressDialog != null && !roundProgressDialog.isShowing()) {
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
        runOnUiThread(() -> {
            if (roundProgressDialog != null && !roundProgressDialog.isShowing()) {
                roundProgressDialog.showProgressDialog(msg);
            }
        });
    }

    /**
     * 关闭圆形加载对话框
     */
    @Override
    public void closeRoundProgressDialog() {
        runOnUiThread(() -> {
            if (roundProgressDialog != null && roundProgressDialog.isShowing()) {
                roundProgressDialog.closeProgressDialog();
            }
        });
    }

    @Override
    public void showNoActionSnackbar(String msg) {
        snackbar = Snackbar.make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
