package com.zhangke.shizhong.model.plan;

import com.zhangke.shizhong.db.Plan;

/**
 * 计划列表的数据实体
 * Created by ZhangKe on 2018/5/18.
 */
public class ShowPlanEntity {

    /**
     * 0-显示普通计划
     * 1-显示添加计划View
     */
    private int type;
    private Plan plan;

    public ShowPlanEntity(int type, Plan plan) {
        this.type = type;
        this.plan = plan;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
