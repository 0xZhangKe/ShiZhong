package com.zhangke.shizhong.page.application;

import android.content.Context;

import com.zhangke.shizhong.db.ApplicationInfo;
import com.zhangke.shizhong.db.ApplicationInfoDao;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.util.ApplicationInfoUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * APP 统计
 * Created by ZhangKe on 2018/4/26.
 */

public class ApplicationStatisticPresenterImpl implements IApplicationStatisticContract.Presenter {

    private Context context;
    private IApplicationStatisticContract.View applicationStatisticView;

    public static List<ApplicationInfo> appOriginList = new ArrayList<>();
    private List<ApplicationInfo> listData = new ArrayList<>();

    private ApplicationInfoDao mApplicationInfoDao;

    private Disposable disposable;

    public ApplicationStatisticPresenterImpl(Context context, IApplicationStatisticContract.View applicationStatisticView) {
        this.context = context;
        this.applicationStatisticView = applicationStatisticView;

        mApplicationInfoDao = DBManager.getInstance().getApplicationInfoDao();
    }

    @Override
    public void getApplication(final boolean showSystem) {
        applicationStatisticView.showTitleProgress();
        Observable<Integer> refreshObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                if (appOriginList.isEmpty()) {
                    appOriginList.addAll(mApplicationInfoDao.loadAll());
                    if (appOriginList.isEmpty()) {
                        appOriginList.addAll(ApplicationInfoUtil.getAllProgramInfo(context));
                        Collections.sort(appOriginList);
                        fillList(showSystem);
                        observableEmitter.onComplete();
                        mApplicationInfoDao.insertOrReplaceInTx(appOriginList);
                    } else {
                        fillList(showSystem);
                        observableEmitter.onNext(1);
                        appOriginList.clear();
                        appOriginList.addAll(ApplicationInfoUtil.getAllProgramInfo(context));
                        Collections.sort(appOriginList);
                        fillList(showSystem);
                        observableEmitter.onComplete();
                        mApplicationInfoDao.deleteAll();
                        mApplicationInfoDao.insertOrReplaceInTx(appOriginList);
                    }
                } else {
                    fillList(showSystem);
                    observableEmitter.onComplete();
                }
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                ApplicationStatisticPresenterImpl.this.disposable = disposable;
            }

            @Override
            public void onNext(Integer position) {
                applicationStatisticView.notifyDataChanged(listData);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                applicationStatisticView.notifyDataChanged(listData);
                applicationStatisticView.closeTitleProgress();
            }
        };
        refreshObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void fillList(boolean showSystem) {
        if (!listData.isEmpty()) {
            listData.clear();
        }
        if (showSystem) {
            listData.addAll(appOriginList);
        } else {
            for (ApplicationInfo app : appOriginList) {
                if (!app.getIsSystemApp()) {
                    listData.add(app);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        applicationStatisticView = null;
        context = null;
        appOriginList.clear();
        listData.clear();
    }
}
