package com.zhangke.shizhong.page.plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Plan;
import com.zhangke.shizhong.db.PlanDao;
import com.zhangke.shizhong.event.PlanChangedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 添加计划界面
 * <p>
 * Created by ZhangKe on 2018/5/19.
 */
public class AddPlanActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_start_date)
    EditText etStartDate;
    @BindView(R.id.et_finish_date)
    EditText etFinishDate;
    @BindView(R.id.et_target_value)
    EditText etTargetValue;
    @BindView(R.id.et_cur_value)
    EditText etCurValue;
    @BindView(R.id.et_unit)
    EditText etUnit;

    private PlanDao mPlanDao;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_plan;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "添加计划", true);

        mPlanDao = DBManager.getInstance().getPlanDao();
    }

    @OnClick(R.id.btn_add)
    public void addPlanBtnClick() {
        String name = etName.getText().toString();
        String startDate = etStartDate.getText().toString();
        String finishDate = etFinishDate.getText().toString();
        String targetValue = etTargetValue.getText().toString();
        String curValue = etCurValue.getText().toString();
        String unit = etUnit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showNoActionSnackbar("请输入计划名");
            return;
        }
        if (TextUtils.isEmpty(startDate)) {
            showNoActionSnackbar("请输入开始日期");
            return;
        }
        if (TextUtils.isEmpty(finishDate)) {
            showNoActionSnackbar("请输入结束日期");
            return;
        }
        if (TextUtils.isEmpty(targetValue)) {
            showNoActionSnackbar("请输入目标值");
            return;
        }
        if (TextUtils.isEmpty(curValue)) {
            showNoActionSnackbar("请输入当前值");
            return;
        }
        if (TextUtils.isEmpty(unit)) {
            showNoActionSnackbar("请输入单位");
            return;
        }
        final Plan plan = new Plan();
        plan.setName(name);
        plan.setStartDate(startDate);
        plan.setFinishDate(finishDate);
        plan.setTarget(Double.valueOf(targetValue));
        plan.setCurrent(Double.valueOf(curValue));
        plan.setUnit(unit);
        Observable.create(e -> {
            mPlanDao.insert(plan);
            e.onNext(1);
            e.onComplete();
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    EventBus.getDefault().post(new PlanChangedEvent());
                    finish();
                });
    }

}
