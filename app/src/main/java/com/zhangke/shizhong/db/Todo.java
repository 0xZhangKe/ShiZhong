package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 待做事项
 *
 * Created by ZhangKe on 2018/7/23.
 */
@Entity
public class Todo {

    @Id(autoincrement = true)
    private Long id;
    private String title;//内容
    private String date;//日期时间
    private boolean completed;//是否已完成
    @Generated(hash = 81639775)
    public Todo(Long id, String title, String date, boolean completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.completed = completed;
    }
    @Generated(hash = 1698043777)
    public Todo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean getCompleted() {
        return this.completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }



}
