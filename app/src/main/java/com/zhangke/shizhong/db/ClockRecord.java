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
public class ClockRecord {

    @Id(autoincrement = true)
    private Long id;
    private Long parentPlanId;
    private String name;
    /**
     * 本次完成值
     */
    private double value;
    @Generated(hash = 1876351570)
    public ClockRecord(Long id, Long parentPlanId, String name, double value) {
        this.id = id;
        this.parentPlanId = parentPlanId;
        this.name = name;
        this.value = value;
    }
    @Generated(hash = 675671732)
    public ClockRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getParentPlanId() {
        return this.parentPlanId;
    }
    public void setParentPlanId(Long parentPlanId) {
        this.parentPlanId = parentPlanId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
}
