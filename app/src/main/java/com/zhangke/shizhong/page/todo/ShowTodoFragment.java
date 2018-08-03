package com.zhangke.shizhong.page.todo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.todo.IShowTodoContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.db.TodoDao;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.event.TodoChangedEvent;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.presenter.todo.ShowTodoPresenterImpl;
import com.zhangke.shizhong.util.ThemeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 修改计划界面
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class ShowTodoFragment extends BaseFragment implements IShowTodoContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private AlertDialog todoOptionDialog;

    private List<ShowTodoEntity> todoList = new ArrayList<>();
    private ShowTodoAdapter adapter;

    private TodoDao mTodoDao;

    private IShowTodoContract.Presenter presenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show_todo;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        unbinder = ButterKnife.bind(this, rootView);

        initToolbar(toolbar, "TODO List", false);
        setHasOptionsMenu(true);
        mTodoDao = DBManager.getInstance().getTodoDao();

        adapter = new ShowTodoAdapter(mActivity, todoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            if (todoList.get(position).getType() == 1) {
                Intent intent = new Intent(mActivity, AddTodoActivity.class);
                startActivity(intent);
            }else{
                showOptionDialog(todoList.get(position).getTodo());
            }
        });

        presenter = new ShowTodoPresenterImpl(mActivity, this);
        presenter.update();
    }

    private void showOptionDialog(final Todo todo){
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_todo_option, null);
        dialogView.findViewById(R.id.tv_complete).setOnClickListener(view -> {
            todo.setCompleted(true);
            mTodoDao.insertOrReplace(todo);
            EventBus.getDefault().post(new TodoChangedEvent());
            todoOptionDialog.cancel();
        });
        dialogView.findViewById(R.id.tv_delete).setOnClickListener(view -> {
            mTodoDao.delete(todo);
            EventBus.getDefault().post(new TodoChangedEvent());
            todoOptionDialog.cancel();
        });
        todoOptionDialog = new AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .create();
        todoOptionDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ThemeChangedEvent event) {
        ThemeUtils.refreshUI(mActivity, rootView);
        viewToolbarDivider.setBackground(getResources().getDrawable(R.drawable.toolbar_bottom_line));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TodoChangedEvent event) {
        presenter.update();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.show_todo_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.completed){
            startActivity(new Intent(mActivity, CompletedTodoActivity.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void notifyTodoChanged(List<ShowTodoEntity> list) {
        todoList.clear();
        todoList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
