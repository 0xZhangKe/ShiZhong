package com.zhangke.shizhong.page.poster.inputname;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.APPConfig;
import com.zhangke.shizhong.page.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入名字及展示搜索结果界面
 * Created by ZhangKe on 2018/4/15.
 */

public class InputNameActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_search)
    Button btnSearch;

    /**
     * 0-豆瓣电影海报
     * 1-云音乐封面
     */
    private int type = 0;

    @Override
    protected int getLayoutResId() {
        initTheme();
        return R.layout.activity_input_name;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_ARG_01)) {
            type = intent.getIntExtra(INTENT_ARG_01, type);
        }

        initToolbar(toolbar, type == 0 ? "豆瓣电影海报" : "云音乐封面", true);
    }

    private void initTheme() {
        if (APPConfig.getTheme() == 0) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
    }

    @OnClick(R.id.btn_search)
    public void onViewClick(View view) {
        String name = etName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showNoActionSnackbar("请输入用户名");
            return;
        }
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(INTENT_ARG_01, type);
        intent.putExtra(INTENT_ARG_02, etName.getText().toString());
        startActivity(intent);
    }

}
