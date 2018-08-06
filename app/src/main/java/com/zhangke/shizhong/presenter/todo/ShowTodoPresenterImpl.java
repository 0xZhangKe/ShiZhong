package com.zhangke.shizhong.presenter.todo;

import android.content.Context;

import com.zhangke.shizhong.contract.todo.IShowTodoContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.db.TodoDao;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private String dateFormat = "yyyy-MM-dd HH:mm";
    private String currentDate = DateUtils.getCurrentDate(dateFormat);

    private List<ShowTodoEntity> listData = new ArrayList<>();

    public ShowTodoPresenterImpl(Context context, IShowTodoContract.View view) {
        this.context = context;
        this.view = view;

        mTodoDao = DBManager.getInstance().getTodoDao();

        updateTodoObservable = Observable.create(e -> {
            listData.clear();
            List<Todo> todoList = mTodoDao.queryBuilder()
                    .where(TodoDao.Properties.Completed.eq(false))
                    .orderAsc(TodoDao.Properties.Date)
                    .list();
            if (!todoList.isEmpty()) {
                listData.addAll(Observable.fromIterable(todoList)
                        .map(todo -> {
                            ShowTodoEntity showTodo = new ShowTodoEntity();
                            showTodo.setType(0);
                            showTodo.setTodo(todo);
                            showTodo.setLevel(getLevel(todo));
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

    /**
     * 获取待办事项紧急等级
     */
    private int getLevel(Todo todo) {
        int level = 1;
        int hourSpace = DateUtils.getHourSpace(dateFormat, currentDate, todo.getDate());
        if (hourSpace <= 1) {
            level = 6;
        } else if (hourSpace <= 5) {
            level = 5;
        } else if (hourSpace <= 12) {
            level = 4;
        } else if (hourSpace <= 24) {
            level = 3;
        } else if (hourSpace <= 48) {
            level = 2;
        }
        return level;
    }

    @Override
    public void update() {
        updateTodoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> view.notifyTodoChanged(listData));

    }
}
