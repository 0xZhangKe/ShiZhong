package com.zhangke.shizhong.page.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.db.TodoDao;
import com.zhangke.shizhong.event.TodoChangedEvent;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 显示待办事项列表
 * <p>
 * Created by ZhangKe on 2018/7/24.
 */
public class ShowTodoAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.ViewHolder, ShowTodoEntity> {

    private TodoDao mTodoDao;

    public ShowTodoAdapter(Context context, List<ShowTodoEntity> listData) {
        super(context, listData);
        mTodoDao = DBManager.getInstance().getTodoDao();
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
        if (holder instanceof TodoHolder) {
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

        private AlertDialog todoOptionDialog;

        TodoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(v -> {
                View dialogView = inflater.inflate(R.layout.dialog_todo_option, null);
                dialogView.findViewById(R.id.tv_complete).setOnClickListener(view -> {
                    Todo todo = listData.get(getAdapterPosition()).getTodo();
                    todo.setCompleted(true);
                    mTodoDao.insertOrReplace(todo);
                    EventBus.getDefault().post(new TodoChangedEvent());
                    todoOptionDialog.cancel();
                });
                dialogView.findViewById(R.id.tv_delete).setOnClickListener(view -> {
                    mTodoDao.delete(listData.get(getAdapterPosition()).getTodo());
                    EventBus.getDefault().post(new TodoChangedEvent());
                    todoOptionDialog.cancel();
                });
                todoOptionDialog = new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .create();
                todoOptionDialog.show();
                return true;
            });
        }
    }

    class AddHolder extends ViewHolder {

        AddHolder(View itemView) {
            super(itemView);
        }
    }
}
