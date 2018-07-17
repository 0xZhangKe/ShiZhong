package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 短期计划实体
 * <p>
 * Created by ZhangKe on 2018/5/9.
 */

@Entity
public class RationRecord {

    @Id(autoincrement = true)
    private Long id;
    private Long parentPlanId;
    private String name;
    /**
     * 本次完成值
     */
    private double value;
    /**
     * 日期
     */
    private String date;
    @Generated(hash = 1133326716)
    public RationRecord(Long id, Long parentPlanId, String name, double value,
            String date) {
        this.id = id;
        this.parentPlanId = parentPlanId;
        this.name = name;
        this.value = value;
        this.date = date;
    }
    @Generated(hash = 1984267879)
    public RationRecord() {
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
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
