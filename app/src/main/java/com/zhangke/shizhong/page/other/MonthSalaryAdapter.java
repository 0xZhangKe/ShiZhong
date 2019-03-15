package com.zhangke.shizhong.page.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.model.other.MonthSalaryEntity;
import com.zhangke.shizhong.page.base.BaseRecyclerAdapter;
import com.zhangke.shizhong.util.NumberFormatUtil;
import com.zhangke.shizhong.util.SimpleTextWatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZhangKe on 2019/3/15.
 */
public class MonthSalaryAdapter extends BaseRecyclerAdapter<MonthSalaryAdapter.ViewHolder, MonthSalaryEntity> {

    private static final String TAG = "MonthSalaryAdapter";

    public MonthSalaryAdapter(Context context, List<MonthSalaryEntity> listData) {
        super(context, listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_month_salary, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MonthSalaryEntity item = listData.get(position);
        holder.textInputLayout.setHint(getHint(item.getMonth()));
        holder.editText.setText(NumberFormatUtil.formatPoint(item.getSalary()));
        holder.editText.addTextChangedListener(new MyTextWatcher(item.getMonth()));
    }

    class MyTextWatcher extends SimpleTextWatcher {

        private int month;

        MyTextWatcher(int month) {
            this.month = month;
        }

        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (TextUtils.isEmpty(s)) return;
            if (listData.isEmpty()) return;
            for (MonthSalaryEntity item : listData) {
                if (item.getMonth() == month) {
                    if (!TextUtils.isEmpty(s)) {
                        try {
                            item.setSalary(Double.valueOf(String.valueOf(s)));
                        } catch (Throwable e) {
                            Log.e(TAG, "afterTextChanged: ", e);
                        }
                    }
                }
            }
        }
    }

    private String getHint(int month) {
        String hint;
        switch (month) {
            case 1:
                hint = "一";
                break;
            case 2:
                hint = "二";
                break;
            case 3:
                hint = "三";
                break;
            case 4:
                hint = "四";
                break;
            case 5:
                hint = "五";
                break;
            case 6:
                hint = "六";
                break;
            case 7:
                hint = "七";
                break;
            case 8:
                hint = "八";
                break;
            case 9:
                hint = "九";
                break;
            case 10:
                hint = "十";
                break;
            case 11:
                hint = "十一";
                break;
            case 12:
            default:
                hint = "十二";
                break;
        }
        return hint + "份月工资";
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {

        @BindView(R.id.text_input_layout)
        TextInputLayout textInputLayout;
        @BindView(R.id.edit_text)
        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
