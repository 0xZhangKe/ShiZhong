package com.zhangke.shizhong.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.zhangke.shizhong.db.ApplicationInfo;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.db.ShortPlan;

import com.zhangke.shizhong.db.ApplicationInfoDao;
import com.zhangke.shizhong.db.PlanDao;
import com.zhangke.shizhong.db.ShortPlanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig applicationInfoDaoConfig;
    private final DaoConfig planDaoConfig;
    private final DaoConfig shortPlanDaoConfig;

    private final ApplicationInfoDao applicationInfoDao;
    private final PlanDao planDao;
    private final ShortPlanDao shortPlanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        applicationInfoDaoConfig = daoConfigMap.get(ApplicationInfoDao.class).clone();
        applicationInfoDaoConfig.initIdentityScope(type);

        planDaoConfig = daoConfigMap.get(PlanDao.class).clone();
        planDaoConfig.initIdentityScope(type);

        shortPlanDaoConfig = daoConfigMap.get(ShortPlanDao.class).clone();
        shortPlanDaoConfig.initIdentityScope(type);

        applicationInfoDao = new ApplicationInfoDao(applicationInfoDaoConfig, this);
        planDao = new PlanDao(planDaoConfig, this);
        shortPlanDao = new ShortPlanDao(shortPlanDaoConfig, this);

        registerDao(ApplicationInfo.class, applicationInfoDao);
        registerDao(Plan.class, planDao);
        registerDao(ShortPlan.class, shortPlanDao);
    }
    
    public void clear() {
        applicationInfoDaoConfig.clearIdentityScope();
        planDaoConfig.clearIdentityScope();
        shortPlanDaoConfig.clearIdentityScope();
    }

    public ApplicationInfoDao getApplicationInfoDao() {
        return applicationInfoDao;
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public ShortPlanDao getShortPlanDao() {
        return shortPlanDao;
    }

}