package com.zhangke.shizhong.presenter.todo;

import com.zhangke.shizhong.contract.todo.ICompletedTodoContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.db.TodoDao;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 已完成的待办事项
 *
 * Created by ZhangKe on 2018/7/28.
 */
public class CompletedTodoPresenterImpl implements ICompletedTodoContract.Presenter {

    private ICompletedTodoContract.View view;


    private TodoDao mTodoDao;
    private Observable<Integer> updateTodoObservable;

    private List<ShowTodoEntity> listData = new ArrayList<>();

    public CompletedTodoPresenterImpl(ICompletedTodoContract.View view) {
        this.view = view;

        mTodoDao = DBManager.getInstance().getTodoDao();

        updateTodoObservable = Observable.create(e -> {
            listData.clear();
            List<Todo> todoList = mTodoDao.queryBuilder()
                    .where(TodoDao.Properties.Completed.eq(true))
                    .orderAsc(TodoDao.Properties.Date)
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
