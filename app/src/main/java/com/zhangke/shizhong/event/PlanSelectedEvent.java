package com.zhangke.shizhong.event;

/**
 * 计划类型选择完成后事件
 * Created by ZhangKe on 2018/7/17.
 */
public class PlanSelectedEvent {

    /**
     * 0-定量计划
     * 1-打卡计划
     */
    private int planType;

    public PlanSelectedEvent(int planType) {
        this.planType = planType;
    }

    /**
     * 0-定量计划
     * 1-打卡计划
     */
    public int getPlanType() {
        return planType;
    }

    /**
     * 0-定量计划
     * 1-打卡计划
     */
    public void setPlanType(int planType) {
        this.planType = planType;
    }
}
