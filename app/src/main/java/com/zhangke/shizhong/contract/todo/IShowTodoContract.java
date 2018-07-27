package com.zhangke.shizhong.contract.todo;

import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 待办事项
 * <p>
 * Created by ZhangKe on 2018/7/23.
 */
public interface IShowTodoContract {

    interface View extends IBasePage{
        void notifyTodoChanged(List<ShowTodoEntity> todoList);
    }

    interface Presenter{
        void update();
    }
}
