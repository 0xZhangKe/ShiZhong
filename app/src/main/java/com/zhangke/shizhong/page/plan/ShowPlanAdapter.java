package com.zhangke.shizhong.page.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 计划列表的适配器
 * Created by ZhangKe on 2018/5/17.
 */
public class ShowPlanAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.ViewHolder, ShowPlanEntity> {

    public ShowPlanAdapter(Context context, List<ShowPlanEntity> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == 0 ?
                new ShowPlanViewHolder(inflater.inflate(R.layout.adapter_show_plan, parent, false)) :
                new AddPlanViewHolder(inflater.inflate(R.layout.adapter_add_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        if (holder instanceof ShowPlanViewHolder) {
            ShowPlanViewHolder showPlanViewHolder = (ShowPlanViewHolder) holder;
            Plan plan = listData.get(position).getPlan();
            showPlanViewHolder.tvPlanName.setText(plan.getName());
            showPlanViewHolder.tvPlanDescription.setText(plan.getDescription());
            showPlanViewHolder.tvStartDate.setText(plan.getStartDate());
            showPlanViewHolder.tvFinishDate.setText(plan.getFinishDate());
            showPlanViewHolder.tvTarget.setText(String.format("%s %s", plan.getTarget(), plan.getUnit()));
            showPlanViewHolder.tvCurrent.setText(String.format("%s %s", plan.getCurrent(), plan.getUnit()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getType();
    }

    class ShowPlanViewHolder extends ViewHolder {

        @BindView(R.id.tv_plan_name)
        TextView tvPlanName;
        @BindView(R.id.tv_plan_description)
        TextView tvPlanDescription;
        @BindView(R.id.tv_start_date)
        TextView tvStartDate;
        @BindView(R.id.tv_finish_date)
        TextView tvFinishDate;
        @BindView(R.id.tv_target)
        TextView tvTarget;
        @BindView(R.id.tv_current)
        TextView tvCurrent;

        ShowPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AddPlanViewHolder extends ViewHolder {
        AddPlanViewHolder(View itemView) {
            super(itemView);
        }
    }
}
