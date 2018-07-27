package com.zhangke.shizhong.page.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.todo.ICompletedTodoContract;
import com.zhangke.shizhong.contract.todo.IShowTodoContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.db.TodoDao;
import com.zhangke.shizhong.event.TodoChangedEvent;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.presenter.todo.CompletedTodoPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已完成的待办事项
 * <p>
 * Created by ZhangKe on 2018/7/28.
 */
public class CompletedTodoActivity extends BaseActivity implements ICompletedTodoContract.View {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<ShowTodoEntity> todoList = new ArrayList<>();
    private ShowTodoAdapter adapter;

    private TodoDao mTodoDao;

    private ICompletedTodoContract.Presenter presenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_complete_todo;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar(toolbar, "已完成的", true);

        mTodoDao = DBManager.getInstance().getTodoDao();

        adapter = new ShowTodoAdapter(this, todoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            if (todoList.get(position).getType() == 0) {
                showOptionDialog(todoList.get(position).getTodo());
            }
        });

        presenter = new CompletedTodoPresenterImpl(this);
        presenter.update();
    }

    private void showOptionDialog(final Todo todo) {
        new AlertDialog.Builder(this)
                .setTitle("确定删除？")
                .setNegativeButton("不不不", null)
                .setPositiveButton("删吧", (DialogInterface dialog, int which) -> {
                    mTodoDao.delete(todo);
                    EventBus.getDefault().post(new TodoChangedEvent());
                })
                .create()
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TodoChangedEvent event) {
        presenter.update();
    }

    @Override
    public void notifyTodoChanged(List<ShowTodoEntity> list) {
        todoList.clear();
        todoList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
