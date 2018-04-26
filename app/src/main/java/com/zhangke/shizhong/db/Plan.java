package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * 计划实体
 * Created by ZhangKe on 2018/4/26.
 */

@Entity
public class Plan {

    @Id(autoincrement = true)
    private long id;
    private String planName;
    private String planDescription;
    private String startDate;
    private String finishDate;
    @Generated(hash = 392513581)
    public Plan(long id, String planName, String planDescription, String startDate,
            String finishDate) {
        this.id = id;
        this.planName = planName;
        this.planDescription = planDescription;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
    @Generated(hash = 592612124)
    public Plan() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPlanName() {
        return this.planName;
    }
    public void setPlanName(String planName) {
        this.planName = planName;
    }
    public String getPlanDescription() {
        return this.planDescription;
    }
    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
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

    
}
