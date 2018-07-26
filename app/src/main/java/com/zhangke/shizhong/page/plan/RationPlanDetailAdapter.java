package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 定量计划打卡记录
 * Created by ZhangKe on 2018/7/26.
 */
public class RationPlanDetailAdapter extends BaseRecyclerAdapter<RationPlanDetailAdapter.ViewHolder, RationRecord> {

    public RationPlanDetailAdapter(Context context, List<RationRecord> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_ration_plan_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RationRecord record = listData.get(position);
        holder.tvTitle.setText(record.getName());
        holder.tvTime.setText(record.getDate());
        holder.tvValue.setText(String.valueOf(record.getValue()));
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_value)
        TextView tvValue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
