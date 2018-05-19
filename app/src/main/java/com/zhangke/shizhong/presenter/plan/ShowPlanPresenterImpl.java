package com.zhangke.shizhong.presenter.plan;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IShowPlanContract;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.db.PlanDao;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static java.util.stream.Collectors.toList;

/**
 * 显示计划界面的业务逻辑及数据层
 * Created by ZhangKe on 2018/5/9.
 */

public class ShowPlanPresenterImpl implements IShowPlanContract.Presenter {

    private IShowPlanContract.View mShowPlanView;

    private PlanDao mPlanDao;
    private List<ShowPlanEntity> mPlanList = new ArrayList<>();

    private Observable<List<ShowPlanEntity>> planObservable;
    private Disposable planDisposable;

    public ShowPlanPresenterImpl(IShowPlanContract.View mShowPlanView) {
        this.mShowPlanView = mShowPlanView;

        mPlanDao = DBManager.getInstance().getPlanDao();

        initObservable();
    }

    private void initObservable() {
        planObservable =
                Observable.create((ObservableEmitter<List<ShowPlanEntity>> e) -> {
                    List<Plan> plans = mPlanDao.queryBuilder().build().list();
                    List<ShowPlanEntity> showPlanList =
                            Observable.fromIterable(plans)
                                    .map(plan -> new ShowPlanEntity(0, plan))
                                    .toList()
                                    .blockingGet();
                    showPlanList.add(new ShowPlanEntity(1, null));
                    e.onNext(showPlanList);
                    e.onComplete();
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getPlanData() {
        mShowPlanView.showRoundProgressDialog();
        mPlanList.clear();

        planObservable.subscribe(new Observer<List<ShowPlanEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {
                planDisposable = d;
            }

            @Override
            public void onNext(List<ShowPlanEntity> value) {
                mPlanList.addAll(value);
                mShowPlanView.notifyPlanDataChanged(mPlanList);
                mShowPlanView.closeRoundProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                mShowPlanView.notifyPlanDataChanged(mPlanList);
                mShowPlanView.closeRoundProgressDialog();
                mShowPlanView.showNoActionSnackbar(mShowPlanView.getContext().getResources().getString(R.string.get_data_error));
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDestroy() {
        mShowPlanView = null;
        mPlanDao = null;
        mPlanList.clear();
        mPlanList = null;
        if (planDisposable != null && !planDisposable.isDisposed()) {
            planDisposable.dispose();
        }
    }
}
