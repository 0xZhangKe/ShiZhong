package com.zhangke.shizhong.page.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZhangKe on 2018/7/24.
 */
public class ShowTodoAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.ViewHolder, ShowTodoEntity> {

    public ShowTodoAdapter(Context context, List<ShowTodoEntity> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == 0 ?
                new TodoHolder(inflater.inflate(R.layout.adapter_show_todo, parent, false)) :
                new AddHolder(inflater.inflate(R.layout.adapter_add_todo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        if(holder instanceof TodoHolder){
            TodoHolder todoHolder = (TodoHolder) holder;
            todoHolder.tvTime.setText(listData.get(position).getTodo().getDate());
            todoHolder.tvTitle.setText(listData.get(position).getTodo().getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listData.get(position).getType();
    }

    class TodoHolder extends ViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        TodoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AddHolder extends ViewHolder {

        AddHolder(View itemView) {
            super(itemView);
        }
    }
}
