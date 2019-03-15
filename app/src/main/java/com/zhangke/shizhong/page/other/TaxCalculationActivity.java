package com.zhangke.shizhong.page.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.model.other.MonthSalaryEntity;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.util.SimpleTextWatcher;
import com.zhangke.shizhong.util.TaxHelper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个税计算页面
 * <p>
 * Created by ZhangKe on 2019/3/15.
 */
public class TaxCalculationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_toolbar_divider)
    View viewToolbarDivider;
    @BindView(R.id.et_salary)
    EditText etSalary;
    @BindView(R.id.recycler_salary_group)
    RecyclerView recyclerSalaryGroup;
    @BindView(R.id.et_security)
    EditText etSecurity;
    @BindView(R.id.et_special)
    EditText etSpecial;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.tv_out_put)
    TextView tvOutPut;

    private final Queue<MonthSalaryEntity> mPool = new ArrayDeque<>(12);

    private List<MonthSalaryEntity> listData = new ArrayList<>(12);
    private MonthSalaryAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tax_calulation;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initToolbar(toolbar, "个税计算", true);

        adapter = new MonthSalaryAdapter(this, listData);
        recyclerSalaryGroup.setLayoutManager(new LinearLayoutManager(this));
        recyclerSalaryGroup.setAdapter(adapter);

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(300);
        defaultItemAnimator.setRemoveDuration(300);
        recyclerSalaryGroup.setItemAnimator(defaultItemAnimator);

        etSalary.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                updateListData();
            }
        });
    }

    @OnClick({R.id.tv_ok, R.id.img_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok: {
                if (TextUtils.isEmpty(etSalary.getText())) {
                    showNoActionSnackbar("请输入工资");
                    return;
                }
                double[] salary = new double[12];
                if (recyclerSalaryGroup.getVisibility() == View.VISIBLE && listData.size() == 12) {
                    for (int i = 0; i < salary.length; i++) {
                        salary[i] = listData.get(i).getSalary();
                    }
                } else {
                    double salaryInput = getDoubleFromString(etSalary.getText().toString());
                    for (int i = 0; i < 12; i++) {
                        salary[i] = salaryInput;
                    }
                }
                calculationTax(salary);
                break;
            }
            case R.id.img_edit: {
                if (recyclerSalaryGroup.getVisibility() != View.VISIBLE) {
                    recyclerSalaryGroup.setVisibility(View.VISIBLE);
                    updateListData();
                }
                break;
            }
        }
    }

    private void calculationTax(double[] salary) {
        double security = getDoubleFromString(etSecurity.getText().toString());
        double special = getDoubleFromString(etSpecial.getText().toString());
        tvOutPut.setText(TaxHelper.getTaxDescription(salary, security, new double[]{special}));
    }

    private void updateListData() {
        if (recyclerSalaryGroup.getVisibility() != View.VISIBLE) {
            return;
        }
        double salary;
        if (TextUtils.isEmpty(etSalary.getText())) {
            salary = 0;
        } else {
            salary = getDoubleFromString(etSalary.getText().toString());
        }
        if (!listData.isEmpty()) {
            for (MonthSalaryEntity item : listData) {
                putCache(item);
            }
            listData.clear();
        }
        for (int i = 1; i < 13; i++) {
            MonthSalaryEntity entity = getMonthEntity();
            entity.setMonth(i);
            entity.setSalary(salary);
            listData.add(entity);
        }
        adapter.notifyDataSetChanged();
    }

    private void putCache(MonthSalaryEntity entity) {
        mPool.offer(entity);
    }

    private MonthSalaryEntity getMonthEntity() {
        MonthSalaryEntity entity = mPool.poll();
        if (entity == null) {
            entity = new MonthSalaryEntity();
        }
        return entity;
    }

    private double getDoubleFromString(String s) {
        try {
            return Double.valueOf(s);
        } catch (Throwable e) {
            return 0.0;
        }
    }
}
