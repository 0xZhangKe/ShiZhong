package com.zhangke.shizhong.page.todo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.db.DBManager;
import com.zhangke.shizhong.db.Todo;
import com.zhangke.shizhong.event.TodoChangedEvent;
import com.zhangke.shizhong.page.base.BaseActivity;
import com.zhangke.shizhong.util.DateTimePickerHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 添加待办事项
 * <p>
 * Created by ZhangKe on 2018/7/24.
 */
public class AddTodoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_date)
    EditText etDate;
    @BindView(R.id.fl_start_date)
    FrameLayout flStartDate;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_todo;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        initToolbar(toolbar, "添加TODO", true);
    }

    @OnClick(R.id.btn_add)
    public void onAddTodoClick() {
        final String title = etTitle.getText().toString();
        final String date = etDate.getText().toString();

        if (TextUtils.isEmpty(title)) {
            showNoActionSnackbar("请输入内容");
            return;
        }

        if (TextUtils.isEmpty(date)) {
            showNoActionSnackbar("请输入时间");
            return;
        }

        Observable.create((ObservableEmitter<Integer> e) -> {
            Todo todo = new Todo();
            todo.setDate(date);
            todo.setTitle(title);
            todo.setCompleted(false);
            DBManager.getInstance().getTodoDao().insert(todo);
            DBManager.getInstance().clear();
            e.onNext(0);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer == 0) {
                        showNoActionSnackbar("已添加");
                        EventBus.getDefault().post(new TodoChangedEvent());
                        new Handler(Looper.getMainLooper()).postDelayed(AddTodoActivity.this::finish, 500);
                    }
                });
    }

    @OnClick(R.id.et_date)
    public void onDateClick() {
        String input = etDate.getText().toString();
        String selectDate = "", selectTime = "";
        if (!TextUtils.isEmpty(input) && input.contains(" ")) {
            String[] array = input.split(" ");
            if (array.length == 2) {
                selectDate = array[0];
                selectTime = array[1];
            }
        }
        DateTimePickerHelper.showDateTimePicker(this,
                "yyyy-MM-dd",
                "HH:mm",
                TextUtils.isEmpty(selectDate) ? "" : selectDate,
                TextUtils.isEmpty(selectTime) ? "" : selectTime,
                etDate::setText);
    }

}
