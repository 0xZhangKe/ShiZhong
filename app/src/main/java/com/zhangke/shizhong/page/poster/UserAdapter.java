package com.zhangke.shizhong.page.poster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhangke.shizhong.R;
import com.zhangke.shizhong.model.poster.UserBean;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.GlideCircleTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户列表
 * <p>
 * Created by ZhangKe on 2018/4/18.
 */

public class UserAdapter extends BaseRecyclerAdapter<UserAdapter.ViewHolder, UserBean>{

    public UserAdapter(Context context, List<UserBean> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_search_result, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserBean itemBean = listData.get(position);
        holder.tv_name.setText(itemBean.getNickName());
        holder.tv_desc.setText(itemBean.getDescription());

        Glide.with(context)
                .load(itemBean.getUserIcon())
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(holder.img_icon);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder{

        @BindView(R.id.img_icon)
        ImageView img_icon;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
