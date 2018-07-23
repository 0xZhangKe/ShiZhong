package com.zhangke.shizhong.page.todo;

import android.support.v7.widget.RecyclerView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IShowTodoContract;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.event.TodoChangedEvent;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.util.ThemeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 修改计划界面
 * <p>
 * Created by ZhangKe on 2018/4/15.
 */

public class ShowTodoFragment extends BaseFragment implements IShowTodoContract.View{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

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
        presenter.update();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ThemeChangedEvent event) {
        ThemeUtils.refreshUI(mActivity, rootView);
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
    public void notifyTodoChanged(List<Todo> todoList) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
