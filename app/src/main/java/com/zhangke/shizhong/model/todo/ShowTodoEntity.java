package com.zhangke.shizhong.model.todo;

import com.zhangke.shizhong.db.Todo;

/**
 * 显示待办事项
 *
 * Created by ZhangKe on 2018/7/24.
 */
public class ShowTodoEntity {

    /**
     * 0-普通代办事项
     * 1-添加
     */
    private int type = 0;

    private Todo todo;

    public ShowTodoEntity() {
    }

    public ShowTodoEntity(int type, Todo todo) {
        this.type = type;
        this.todo = todo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }
}
