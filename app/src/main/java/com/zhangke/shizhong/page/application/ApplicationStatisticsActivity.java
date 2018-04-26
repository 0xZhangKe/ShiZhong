package com.zhangke.shizhong.page.application;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ApplicationInfo;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.ApplicationInfoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * App 统计界面
 * <p>
 * Created by ZhangKe on 2018/4/26.
 */

public class ApplicationStatisticsActivity extends BaseActivity implements IApplicationStatisticContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_app_num)
    TextView tvAppNum;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.switch_system_app)
    Switch switchSystemApp;
    @BindView(R.id.img_show_type)
    ImageView imgShowType;
    @BindView(R.id.card_title_view)
    CardView cardTitleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private IApplicationStatisticContract.Presenter presenter;

    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private List<ApplicationInfo> listData = new ArrayList<>();
    private ApplicationListAdapter adapter;

    private boolean isSingleShow = true;
    private AlertDialog alertDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_application_statistics;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "应用录", true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 2);

        adapter = new ApplicationListAdapter(this, listData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((View view, int position) -> {
            showApplicationInfo(listData.get(position));
        });

        presenter = new ApplicationStatisticPresenterImpl(this, this);
        presenter.getApplication(true);
    }

    public void showApplicationInfo(final ApplicationInfo appInfo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ListView lv = new ListView(this);
        lv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(lv);
        final List<String> data = new ArrayList<>();
        data.add("打开 " + appInfo.getAppName());
        data.add("查看应用信息");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            alertDialog.dismiss();
            switch (position) {
                case 0: {
                    ApplicationInfoUtil.doStartApplicationWithPackageName(this, appInfo.getPackageName());
                    break;
                }
                case 1: {
                    Uri packageURI = Uri.parse("package:" + appInfo.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivity(intent);
                    break;
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @OnClick(R.id.img_show_type)
    public void changeShowType() {
        isSingleShow = !isSingleShow;
        imgShowType.setImageDrawable(getResources().getDrawable(isSingleShow ? R.mipmap.sign_line : R.mipmap.double_line));
        adapter.setSingleShow(isSingleShow);
        if (isSingleShow) {
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    @OnCheckedChanged(R.id.switch_system_app)
    public void onWitchCheckChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            presenter.getApplication(true);
        } else {
            presenter.getApplication(false);
        }
    }

    @Override
    public void notifyDataChanged(List<ApplicationInfo> list) {
        listData.clear();
        listData.addAll(list);
        adapter.notifyDataSetChanged();
        tvAppNum.setText(String.format("APP 数：%s", listData.size()));
    }

    @Override
    public void showTitleProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeTitleProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
