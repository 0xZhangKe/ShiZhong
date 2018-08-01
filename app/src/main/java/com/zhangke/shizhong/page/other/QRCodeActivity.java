package com.zhangke.shizhong.page.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.zxing.Result;
import com.zhangke.qrcodeview.QRCodeView;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 二维码扫描页面
 * <p>
 * Created by ZhangKe on 2018/8/1.
 */
public class QRCodeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.qr_code)
    QRCodeView qrCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "扫描二维码", true);

        qrCode.setOnQRCodeListener(result -> {
            Intent intent = new Intent(QRCodeActivity.this, ShowQRCodeContentActivity.class);
            intent.putExtra(INTENT_ARG_01, result.getText());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCode.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCode.stopPreview();
    }
}
