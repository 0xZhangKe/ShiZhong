package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 *
 * 计划实体
 * Created by ZhangKe on 2018/4/26.
 */

@Entity
public class Plan {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String finishDate;
    /**
     * 当前已完成值
     */
    private double current;
    /**
     * 目标值
     */
    private double target;
    /**
     * 目标单位
     */
    private String unit;

    @ToMany(referencedJoinProperty = "parentPlanId")
    private List<ShortPlan> shortPlanList;

    @Generated(hash = 1531688657)
    public Plan(Long id, String name, String description, String startDate,
            String finishDate, double current, double target, String unit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.current = current;
        this.target = target;
        this.unit = unit;
    }
    @Generated(hash = 592612124)
    public Plan() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStartDate() {
        return this.startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getFinishDate() {
        return this.finishDate;
    }
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
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
