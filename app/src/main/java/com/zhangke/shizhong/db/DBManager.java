package com.zhangke.shizhong.db;

import android.content.Context;

import com.zhangke.shizhong.common.SZApplication;

/**
 * 数据库管理类
 * <p>
 * Created by ZhangKe on 2018/4/26.
 */

public class DBManager {

    private static final String DB_NAME = "data.db";
    private DaoSession daoSession;

    private static DBManager dbManager;

    public static DBManager getInstance() {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager();
                }
            }
        }
        return dbManager;
    }

    public void init(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

        daoSession = daoMaster.newSession();
    }

    public ApplicationInfoDao getApplicationInfoDao() {
        return daoSession.getApplicationInfoDao();
    }

    public PlanDao getPlanDao() {
        return daoSession.getPlanDao();
    }

    public ClockRecordDao getClockRecordDao(){
        return daoSession.getClockRecordDao();
    }
}
