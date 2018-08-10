package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;

/**
 * 计划实体
 * Created by ZhangKe on 2018/4/26.
 */

@Entity
public class RationPlan {

    @Id(autoincrement = true)
    private Long id;
    private String name;
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

    /**
     * 计划类型：
     * -1：未设置
     * 0-攒钱计划
     * 1-减肥计划
     * 10-其他
     */
    private int planType = -1;

    /**
     * 周期计划是否已开启
     */
    private boolean periodIsOpen = false;

    /**
     * 周期计划类型：</br>
     * 0-日；</br>
     * 1-周；</br>
     * 2-月。
     */
    private int periodPlanType;
    /**
     * 每个周期计划的目标值
     */
    private double periodPlanTarget;

    /**
     * 上次更新短期计划目标日期
     */
    private String lastUpdatePeriodDate;

    @ToMany(referencedJoinProperty = "parentPlanId")
    private List<RationRecord> clockRecords;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 135137572)
    private transient RationPlanDao myDao;

    @Generated(hash = 734893141)
    public RationPlan(Long id, String name, String startDate, String finishDate,
            double current, double target, String unit, int planType, boolean periodIsOpen,
            int periodPlanType, double periodPlanTarget, String lastUpdatePeriodDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.current = current;
        this.target = target;
        this.unit = unit;
        this.planType = planType;
        this.periodIsOpen = periodIsOpen;
        this.periodPlanType = periodPlanType;
        this.periodPlanTarget = periodPlanTarget;
        this.lastUpdatePeriodDate = lastUpdatePeriodDate;
    }

    @Generated(hash = 1681540746)
    public RationPlan() {
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

    public int getPlanType() {
        return this.planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public boolean getPeriodIsOpen() {
        return this.periodIsOpen;
    }

    public void setPeriodIsOpen(boolean periodIsOpen) {
        this.periodIsOpen = periodIsOpen;
    }

    public int getPeriodPlanType() {
        return this.periodPlanType;
    }

    public void setPeriodPlanType(int periodPlanType) {
        this.periodPlanType = periodPlanType;
    }

    public double getPeriodPlanTarget() {
        return this.periodPlanTarget;
    }

    public void setPeriodPlanTarget(double periodPlanTarget) {
        this.periodPlanTarget = periodPlanTarget;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1512945595)
    public List<RationRecord> getClockRecords() {
        if (clockRecords == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RationRecordDao targetDao = daoSession.getRationRecordDao();
            List<RationRecord> clockRecordsNew = targetDao
                    ._queryRationPlan_ClockRecords(id);
            synchronized (this) {
                if (clockRecords == null) {
                    clockRecords = clockRecordsNew;
                }
            }
        }
        return clockRecords;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 501215887)
    public synchronized void resetClockRecords() {
        clockRecords = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 572427226)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRationPlanDao() : null;
    }

    public String getLastUpdatePeriodDate() {
        return this.lastUpdatePeriodDate;
    }

    public void setLastUpdatePeriodDate(String lastUpdatePeriodDate) {
        this.lastUpdatePeriodDate = lastUpdatePeriodDate;
    }

}
