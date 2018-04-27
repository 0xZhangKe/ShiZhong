package com.zhangke.shizhong.page.application;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ApplicationInfo;
import com.zhangke.shizhong.db.ApplicationInfoDao;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.util.ApplicationInfoUtil;
import com.zhangke.shizhong.util.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索APP
 * <p>
 * Created by ZhangKe on 2018/4/27.
 */
public class SearchApplicationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AlertDialog alertDialog;

    private ApplicationInfoDao mApplicationInfoDao;

    private List<ApplicationInfo> originAppList = new ArrayList<>();
    private List<ApplicationInfo> listData = new ArrayList<>();
    private ApplicationListAdapter adapter;

    private String curQueryText = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_application;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "搜索", true);
        mApplicationInfoDao = DBManager.getInstance().getApplicationInfoDao();

        ThreadPool.getInstance().getThreadPool().execute(() -> {
            originAppList.addAll(mApplicationInfoDao.loadAll());
            runOnUiThread(SearchApplicationActivity.this::search);
            List<ApplicationInfo> tmp = ApplicationInfoUtil.getAllProgramInfo(SearchApplicationActivity.this);
            originAppList.clear();
            originAppList.addAll(tmp);
            tmp.clear();
            if (!isDestroyed()) {
                runOnUiThread(SearchApplicationActivity.this::search);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ApplicationListAdapter(this, listData);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            showApplicationInfo(listData.get(position));
            search();
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_application_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem.getActionView();
        setupSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchView(final SearchView searchView) {
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                curQueryText = newText;
                search();
                return true;
            }
        });
    }

    private void search() {
        if (originAppList.isEmpty() || TextUtils.isEmpty(curQueryText)) {
            listData.clear();
            adapter.notifyDataSetChanged();
        } else {
            listData.clear();
            for (ApplicationInfo itemApp : originAppList) {
                if (itemApp.getAppName().contains(curQueryText)) {
                    listData.add(itemApp);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
