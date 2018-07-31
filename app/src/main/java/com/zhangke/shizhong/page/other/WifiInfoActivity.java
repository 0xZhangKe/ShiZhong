package com.zhangke.shizhong.page.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.util.WifiManage;

import java.util.List;

/**
 * 查看 wifi 密码
 *
 * Created by ZhangKe on 2018/8/1.
 */
public class WifiInfoActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wifi_info;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);

        initToolbar(toolbar, "查看 wifi 信息", true);

        TextView tvInfo = findViewById(R.id.tv_info);

        List<WifiManage.WifiInfo> list = WifiManage.readWifiInfo();
        StringBuilder builder = new StringBuilder();
        if(list != null && !list.isEmpty()){
            for(WifiManage.WifiInfo info : list){
                builder.append(info.toString());
                builder.append("\n\n");
            }
        }else{
            builder.append("未获取到 wifi 信息，请检查是否允许 root 权限。");
        }
        tvInfo.setText(builder.toString());
    }
}
