package com.zhangke.shizhong.db;

import org.greenrobot.greendao.annotation.Id;

/**
 * 打卡计划
 *
 * Created by ZhangKe on 2018/7/10.
 */
public class ClockPlan {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String startDate;
    private String finishDate;
}
