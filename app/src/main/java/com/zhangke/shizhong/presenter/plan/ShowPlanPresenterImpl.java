package com.zhangke.shizhong.presenter.plan;

import android.text.TextUtils;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.contract.plan.IShowPlanContract;
import com.zhangke.shizhong.db.ClockPlan;
import com.zhangke.shizhong.db.ClockPlanDao;
import com.zhangke.shizhong.db.ClockRecord;
import com.zhangke.shizhong.db.RationPlanDao;
import com.zhangke.shizhong.db.RationRecord;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.RationPlan;
import com.zhangke.shizhong.model.plan.ShowPlanEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 显示计划界面的业务逻辑及数据层
 * Created by ZhangKe on 2018/5/9.
 */

public class ShowPlanPresenterImpl implements IShowPlanContract.Presenter {

    private IShowPlanContract.View mShowPlanView;

    private RationPlanDao mRationPlanDao;
    private ClockPlanDao mClockPlanDao;
    private List<ShowPlanEntity> mPlanList = new ArrayList<>();

    private Observable<List<ShowPlanEntity>> planObservable;
    private Disposable planDisposable;

    public ShowPlanPresenterImpl(IShowPlanContract.View mShowPlanView) {
        this.mShowPlanView = mShowPlanView;

        mRationPlanDao = DBManager.getInstance().getRationPlanDao();
        mClockPlanDao = DBManager.getInstance().getClockPlanDao();

        initObservable();
    }

    private void initObservable() {
        planObservable =
                Observable.create((ObservableEmitter<List<ShowPlanEntity>> e) -> {
                    List<RationPlan> rationPlans = mRationPlanDao.queryBuilder().build().list();
                    List<ClockPlan> clockPlans = mClockPlanDao.queryBuilder().build().list();
                    List<ShowPlanEntity> showPlanList = new ArrayList<>();
                    showPlanList.addAll(convertRationPlan(rationPlans));
                    showPlanList.addAll(convertClockPlan(clockPlans));
                    showPlanList.add(new ShowPlanEntity(2));
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

    private List<ShowPlanEntity> convertRationPlan(List<RationPlan> list) {
        return Observable.fromIterable(list)
                .map(plan -> {
                    ShowPlanEntity showPlanEntity = new ShowPlanEntity();
                    showPlanEntity.setType(0);
                    showPlanEntity.setRationPlan(plan);
                    showPlanEntity.setPlanName(plan.getName());
                    showPlanEntity.setPlanInfo(String.format("%s ~ %s        当前：%s%s",
                            plan.getStartDate(),
                            plan.getFinishDate(),
                            plan.getCurrent(),
                            plan.getUnit()));
                    showPlanEntity.setProgress(PlanHelper.getProgress(plan));
                    showPlanEntity.setSurplus(String.format("剩余：%s%s", plan.getTarget() - plan.getCurrent(), plan.getUnit()));
                    showPlanEntity.setPeriodIsOpen(plan.getPeriodIsOpen());
                    if (plan.getPeriodIsOpen()) {
                        showPlanEntity.setShortPlanTitle(plan.getPeriodPlanType() == 0
                                ? "今日计划" : plan.getPeriodPlanType() == 1
                                ? "本周计划" : "本月计划");
                        showPlanEntity.setShortPlanTarget(String.format("目标：%s%s", plan.getPeriodPlanTarget(), plan.getUnit()));
                        double currentValue = 0.0;
                        List<RationRecord> records = plan.getClockRecords();
                        if (records != null && !records.isEmpty()) {
                            for (RationRecord record : records) {
                                if (PlanHelper.isCurPeriod(plan.getPeriodPlanType(), record)) {
                                    currentValue += record.getValue();
                                }
                            }
                            List<InputCount> suggestionInput = new ArrayList<>();
                            for (RationRecord record : records) {
                                if (!TextUtils.isEmpty(record.getName())) {
                                    InputCount item = new InputCount(record.getName(), 0);
                                    if (suggestionInput.contains(item)) {
                                        int i = suggestionInput.indexOf(item);
                                        suggestionInput.get(i).setCount(suggestionInput.get(i).getCount() + 1);
                                    } else {
                                        suggestionInput.add(new InputCount(record.getName(), 0));
                                    }
                                }
                            }
                            if (!suggestionInput.isEmpty()) {
                                Collections.sort(suggestionInput, (InputCount o1, InputCount o2) -> o2.getCount() - o1.getCount());
                                showPlanEntity.setSuggestionInput(
                                        Observable.fromIterable(suggestionInput)
                                                .map(InputCount::getInput)
                                                .toList()
                                                .blockingGet());
                            }
                        }
                        showPlanEntity.setShortPlanSurplus(String.format("剩余：%s%s", plan.getPeriodPlanTarget() - currentValue, plan.getUnit()));
                    }
                    return showPlanEntity;
                })
                .toList()
                .blockingGet();
    }

    private List<ShowPlanEntity> convertClockPlan(List<ClockPlan> list) {
        return Observable.fromIterable(list)
                .map(plan -> {
                    ShowPlanEntity showPlanEntity = new ShowPlanEntity();
                    showPlanEntity.setType(1);
                    showPlanEntity.setClockPlan(plan);
                    showPlanEntity.setPlanName(plan.getName());
                    showPlanEntity.setPlanInfo(plan.getDescription());
                    List<InputCount> suggestionInput = new ArrayList<>();
                    List<ClockRecord> records = plan.getClockRecords();
                    if (records != null && !records.isEmpty()) {
                        for (ClockRecord record : records) {
                            if (!TextUtils.isEmpty(record.getDescription())) {
                                InputCount item = new InputCount(record.getDescription(), 0);
                                if (suggestionInput.contains(item)) {
                                    int i = suggestionInput.indexOf(item);
                                    suggestionInput.get(i).setCount(suggestionInput.get(i).getCount() + 1);
                                } else {
                                    suggestionInput.add(new InputCount(record.getDescription(), 0));
                                }
                            }
                        }
                        if (!suggestionInput.isEmpty()) {
                            Collections.sort(suggestionInput, (InputCount o1, InputCount o2) -> o1.getCount() - o2.getCount());
                            List<String> suggestion = new ArrayList<>();
                            for (InputCount item : suggestionInput) {
                                suggestion.add(item.getInput());
                            }
                            showPlanEntity.setSuggestionInput(suggestion);
                        }
                    }
                    return showPlanEntity;
                })
                .toList()
                .blockingGet();
    }

    private static class InputCount {
        private String input;
        private int count;

        public InputCount(String input, int count) {
            this.input = input;
            this.count = count;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof InputCount)) {
                return false;
            }
            InputCount o = (InputCount) obj;
            return TextUtils.equals(o.input, input);
        }

        private volatile int hashCode;

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                if (!TextUtils.isEmpty(input)) {
                    result += 31 * result + input.hashCode();
                }
                hashCode = result;
            }
            return result;
        }
    }

    @Override
    public void onDestroy() {
        mShowPlanView = null;
        mClockPlanDao = null;
        mRationPlanDao = null;
        mPlanList.clear();
        mPlanList = null;
        if (planDisposable != null && !planDisposable.isDisposed()) {
            planDisposable.dispose();
        }
    }
}
