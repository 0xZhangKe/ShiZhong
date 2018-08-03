package com.zhangke.shizhong.page.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.util.PermissionUtil;
import com.zhangke.shizhong.util.UiUtils;
import com.zhangke.shizhong.widget.RoundProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Activity 基类
 * Created by ZhangKe on 2018/4/15.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBasePage {

    protected final String TAG = this.getClass().getSimpleName();

    private Snackbar snackbar;

    private RoundProgressDialog roundProgressDialog;

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(getLayoutResId());
        if(showImmersionWindowStatus()) {
            initStatusBar();
        }
        roundProgressDialog = new RoundProgressDialog(this);
        mHandler = new Handler();
        initView(savedInstanceState);
    }

    private void initTheme() {
        if (APPConfig.getTheme() == 0) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
    }

    protected abstract int getLayoutResId();

    protected void initStatusBar(){
        if (APPConfig.getTheme() == 0) {
            UiUtils.setWindowColorRect(this, getResources().getColor(R.color.nightColorPrimary));
            if(Build.VERSION.SDK_INT >= 23) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }else{
            UiUtils.setWindowColorRect(this, getResources().getColor(R.color.dayColorPrimary));
            if(Build.VERSION.SDK_INT >= 23) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    protected boolean showImmersionWindowStatus(){
        return true;
    }

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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private Map<Short, Runnable> requestPermissionMap = new HashMap<>();

    /**
     * 判断是否具有改权限，不具备则申请
     *
     * @param runnable   获取到权限之后做的事情
     * @param permission 权限列表
     */
    protected void checkAndRequestPermission(Runnable runnable, String... permission) {
        List<String> permissionList = new ArrayList<>();
        for (String item : permission) {
            if (PermissionUtil.isLacksOfPermission(this, item)) {
                permissionList.add(item);
            }
        }
        if (permissionList.isEmpty()) {
            mHandler.post(runnable);
        } else {
            short requestCode = getRequestCode();
            requestPermissionMap.put(requestCode, runnable);
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Set<Short> requestCodeSet = requestPermissionMap.keySet();
        if (!requestCodeSet.isEmpty()) {
            for (Short key : requestCodeSet) {
                if (requestCode == (int) key) {
                    boolean get = true;
                    for (int i : grantResults) {
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            get = false;
                            break;
                        }
                    }
                    if (get) {
                        Runnable runnable = requestPermissionMap.get(key);
                        if (runnable != null) {
                            mHandler.post(runnable);
                        }
                        requestPermissionMap.remove(key);
                        break;
                    }
                }
            }
        }
    }

    private short getRequestCode() {
        short requestCode = 1963;
        Set<Short> requestCodeSet = requestPermissionMap.keySet();
        if (!requestCodeSet.isEmpty()) {
            while (requestCodeSet.contains(requestCode)) {
                requestCode = (short) (Math.random() * 100);
            }
        }
        return requestCode;
    }
}
