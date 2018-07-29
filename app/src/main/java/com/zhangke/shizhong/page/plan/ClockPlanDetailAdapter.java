package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 打卡计划详情页打卡记录列表
 * <p>
 * Created by ZhangKe on 2018/7/29.
 */
public class ClockPlanDetailAdapter extends BaseRecyclerAdapter<ClockPlanDetailAdapter.ViewHolder, ClockRecord> {

    public ClockPlanDetailAdapter(Context context, List<ClockRecord> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_clock_plan_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDate.setText(listData.get(position).getDate());
        holder.tvTitle.setText(listData.get(position).getDescription());
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
