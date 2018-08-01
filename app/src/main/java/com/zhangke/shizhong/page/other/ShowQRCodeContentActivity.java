package com.zhangke.shizhong.page.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示扫描到的二维码内容
 * <p>
 * Created by ZhangKe on 2018/8/1.
 */
public class ShowQRCodeContentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_qr_code_content;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "内容", true);

        Intent intent = getIntent();
        if(intent.hasExtra(INTENT_ARG_01)){
            String text = intent.getStringExtra(INTENT_ARG_01);
            tvContent.setText(text);
        }
    }
}
