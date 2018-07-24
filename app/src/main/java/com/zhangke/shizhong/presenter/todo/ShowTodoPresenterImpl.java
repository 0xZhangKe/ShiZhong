package com.zhangke.shizhong.presenter.todo;

import android.content.Context;

import com.zhangke.shizhong.contract.plan.IShowTodoContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.db.TodoDao;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 待办事项展示
 * <p>
 * Created by ZhangKe on 2018/7/24.
 */
public class ShowTodoPresenterImpl implements IShowTodoContract.Presenter {

    private Context context;
    private IShowTodoContract.View view;

    private TodoDao mTodoDao;
    private Observable<Integer> updateTodoObservable;

    private List<ShowTodoEntity> listData = new ArrayList<>();

    public ShowTodoPresenterImpl(Context context, IShowTodoContract.View view) {
        this.context = context;
        this.view = view;

        mTodoDao = DBManager.getInstance().getTodoDao();

        updateTodoObservable = Observable.create(e -> {
            listData.clear();
            List<Todo> todoList = mTodoDao.queryBuilder()
                    .where(TodoDao.Properties.Completed.eq(false))
                    .list();
            if (!todoList.isEmpty()) {
                listData.addAll(Observable.fromIterable(todoList)
                        .map(todo -> {
                            ShowTodoEntity showTodo = new ShowTodoEntity();
                            showTodo.setType(0);
                            showTodo.setTodo(todo);
                            return showTodo;
                        })
                        .toList()
                        .blockingGet());
            }
            ShowTodoEntity addTodoEntity = new ShowTodoEntity();
            addTodoEntity.setType(1);
            listData.add(addTodoEntity);
            e.onNext(0);
            e.onComplete();
        });
    }

    @Override
    public void update() {
        updateTodoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> view.notifyTodoChanged(listData));

    }
}
