package com.zhangke.shizhong.model.plan;

import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.RationPlan;

import java.util.List;

/**
 * 计划列表的数据实体
 * Created by ZhangKe on 2018/5/18.
 */
public class ShowPlanEntity {

    /**
     * 0-显示定量计划
     * 1-显示打卡计划
     * 2-显示添加计划View
     */
    private int type;
    private String planName;
    private List<String> suggestionInput;

    private RationPlan rationPlan;
    private String planInfo;
    private int progress;//0-100
    private String surplus;
    private boolean periodIsOpen;
    private String shortPlanTitle;
    private String shortPlanTarget;
    private String shortPlanSurplus;

    private ClockPlan clockPlan;

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

    public RationPlan getRationPlan() {
        return rationPlan;
    }

    public void setRationPlan(RationPlan rationPlan) {
        this.rationPlan = rationPlan;
    }

    public List<String> getSuggestionInput() {
        return suggestionInput;
    }

    public void setSuggestionInput(List<String> suggestionInput) {
        this.suggestionInput = suggestionInput;
    }

    public ClockPlan getClockPlan() {
        return clockPlan;
    }

    public void setClockPlan(ClockPlan clockPlan) {
        this.clockPlan = clockPlan;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanInfo() {
        return planInfo;
    }

    public void setPlanInfo(String planInfo) {
        this.planInfo = planInfo;
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
