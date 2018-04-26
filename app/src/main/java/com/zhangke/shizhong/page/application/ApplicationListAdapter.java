package com.zhangke.shizhong.page.application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ApplicationInfo;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * APP 列表
 * <p>
 * Created by ZhangKe on 2018/4/26.
 */

public class ApplicationListAdapter extends BaseRecyclerAdapter<ApplicationListAdapter.AppInfoViewHolder, ApplicationInfo> {

    private boolean isSingleShow = true;//单排显示

    ApplicationListAdapter(Context context, List<ApplicationInfo> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public AppInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppInfoViewHolder(
                inflater.inflate(
                        isSingleShow ?
                                R.layout.adapter_single_list :
                                R.layout.adapter_double_list,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoViewHolder holder, int position) {
        ApplicationInfo info = listData.get(position);
        holder.tvDesc.setText(info.getPackageName());
        holder.tvName.setText(info.getAppName());
        holder.imgIcon.setImageDrawable(info.getAppIcon());
    }

    class AppInfoViewHolder extends BaseRecyclerAdapter.ViewHolder {

        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_desc)
        TextView tvDesc;

        AppInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSingleShow(boolean singleShow) {
        isSingleShow = singleShow;
    }
}
