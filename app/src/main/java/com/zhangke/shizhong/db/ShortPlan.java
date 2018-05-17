package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 短期计划实体
 *
 * Created by ZhangKe on 2018/5/9.
 */

@Entity
public class ShortPlan {

    @Id(autoincrement = true)
    private long id;
    private long parentPlanId;
    private int cycleType;
    /**
     * 当前已完成值
     */
    private double current;
    /**
     * 目标值
     */
    private double target;
    private String unit;
    @Generated(hash = 1631705811)
    public ShortPlan(long id, long parentPlanId, int cycleType, double current,
            double target, String unit) {
        this.id = id;
        this.parentPlanId = parentPlanId;
        this.cycleType = cycleType;
        this.current = current;
        this.target = target;
        this.unit = unit;
    }
    @Generated(hash = 1952916159)
    public ShortPlan() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getParentPlanId() {
        return this.parentPlanId;
    }
    public void setParentPlanId(long parentPlanId) {
        this.parentPlanId = parentPlanId;
    }
    public int getCycleType() {
        return this.cycleType;
    }
    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }
    public double getCurrent() {
        return this.current;
    }
    public void setCurrent(double current) {
        this.current = current;
    }
    public double getTarget() {
        return this.target;
    }
    public void setTarget(double target) {
        this.target = target;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }


}