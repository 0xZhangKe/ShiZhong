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
    private String planName;
    private String targetValue;
    private String unit;
    private String planInfo;
    private String finishDate;
    private int progress;//0-100
    private String surplus;
    private boolean periodIsOpen;
    private String shortPlanTitle;
    private String shortPlanTarget;
    private String shortPlanSurplus;

    public ShowPlanEntity() {
    }

    public ShowPlanEntity(int type) {
        this.type = type;
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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPlanInfo() {
        return planInfo;
    }

    public void setPlanInfo(String planInfo) {
        this.planInfo = planInfo;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public boolean isPeriodIsOpen() {
        return periodIsOpen;
    }

    public void setPeriodIsOpen(boolean periodIsOpen) {
        this.periodIsOpen = periodIsOpen;
    }

    public String getShortPlanTitle() {
        return shortPlanTitle;
    }

    public void setShortPlanTitle(String shortPlanTitle) {
        this.shortPlanTitle = shortPlanTitle;
    }

    public String getShortPlanTarget() {
        return shortPlanTarget;
    }

    public void setShortPlanTarget(String shortPlanTarget) {
        this.shortPlanTarget = shortPlanTarget;
    }

    public String getShortPlanSurplus() {
        return shortPlanSurplus;
    }

    public void setShortPlanSurplus(String shortPlanSurplus) {
        this.shortPlanSurplus = shortPlanSurplus;
    }
}
