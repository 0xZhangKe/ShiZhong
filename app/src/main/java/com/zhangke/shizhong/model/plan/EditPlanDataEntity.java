package com.zhangke.shizhong.model.plan;

/**
 * 修改计划界面使用
 * Created by ZhangKe on 2018/7/20.
 */
public class EditPlanDataEntity {

    private String name;
    private String finishDate;
    private String description;
    private double target;
    private double current;
    private String unit;
    private boolean periodIsOpen;
    private double periodTarget;
    private int periodPlanType;

    public EditPlanDataEntity() {
    }

    public EditPlanDataEntity(String name, String finishDate, String description, double target, double current, String unit, boolean periodIsOpen, double periodTarget, int periodPlanType) {
        this.name = name;
        this.finishDate = finishDate;
        this.description = description;
        this.target = target;
        this.current = current;
        this.unit = unit;
        this.periodIsOpen = periodIsOpen;
        this.periodTarget = periodTarget;
        this.periodPlanType = periodPlanType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isPeriodIsOpen() {
        return periodIsOpen;
    }

    public void setPeriodIsOpen(boolean periodIsOpen) {
        this.periodIsOpen = periodIsOpen;
    }

    public double getPeriodTarget() {
        return periodTarget;
    }

    public void setPeriodTarget(double periodTarget) {
        this.periodTarget = periodTarget;
    }

    public int getPeriodPlanType() {
        return periodPlanType;
    }

    public void setPeriodPlanType(int periodPlanType) {
        this.periodPlanType = periodPlanType;
    }
}
