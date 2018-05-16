package com.zhangke.shizhong.page.plan;

import android.graphics.Bitmap;
import android.util.Log;

import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.db.PlanDao;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 显示计划界面的业务逻辑及数据层
 * Created by ZhangKe on 2018/5/9.
 */

public class PlanPresenterImpl implements IShowPlanContract.Presenter {

    private static final String TAG = "PlanPresenterImpl";

    private IShowPlanContract.View mShowPlanView;

    private PlanDao mPlanDao;
    private List<Plan> mPlanList = new ArrayList<>();

    public PlanPresenterImpl(IShowPlanContract.View mShowPlanView) {
        this.mShowPlanView = mShowPlanView;

        mPlanDao = DBManager.getInstance().getPlanDao();
    }

    private Disposable mDisposable;
    private Subscription mSubscription;

    @Override
    public void getPlanData() {
        mShowPlanView.showRoundProgressDialog();
        mPlanList.clear();//todo 使用 RXAndroid 实现
        mPlanList.addAll(mPlanDao.loadAll());
        mShowPlanView.notifyPlanDataChanged(mPlanList);
        mShowPlanView.closeRoundProgressDialog();

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        };

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                subscriber.onNext("hello");
                subscriber.onNext("world");
                subscriber.onComplete();
            }
        });

        Consumer<String> onNext = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //泛型为String，所以用来充当接收 onNext 事件的角色
                Log.d(TAG, s);
            }
        };

        Consumer<Throwable> onError = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //泛型为 Throwable ,用来充当接收 onError 事件的角色
                Log.d(TAG, throwable.toString());
            }
        };

        Action onComplete = new Action() {
            @Override
            public void run() throws Exception {
                //没有参数，用来充当接收 onComplete 事件的角色
                Log.d(TAG, "onComplete");
            }
        };

        //只响应 onNext 事件
        observable.subscribe(onNext);
        //响应 onNext 及 onError 事件
        observable.subscribe(onNext, onError);
        //响应 onNext 、 onError 及 onComplete 事件
        observable.subscribe(onNext, onError, onComplete);
        observable.subscribeOn(Schedulers.computation());
        observable.observeOn(Schedulers.io());
    }

    @Override
    public void onDestroy() {
        mShowPlanView = null;
        mPlanDao = null;
        mPlanList.clear();
        mPlanList = null;
    }
}
