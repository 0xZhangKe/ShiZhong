package com.zhangke.shizhong.contract.todo;

import com.zhangke.shizhong.model.todo.ShowTodoEntity;
import com.zhangke.shizhong.page.base.IBasePage;

import java.util.List;

/**
 * 已完成的待办事项
 *
 * Created by ZhangKe on 2018/7/28.
 */
public interface ICompletedTodoContract {

    interface View extends IBasePage {
        void notifyTodoChanged(List<ShowTodoEntity> todoList);
    }

    interface Presenter{
        void update();
    }
}
