package com.zhangke.shizhong.page.plan;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.event.ThemeChangedEvent;
import com.zhangke.shizhong.page.base.BaseFragment;
import com.zhangke.shizhong.util.ThemeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 显示计划界面
 *
 * Created by ZhangKe on 2018/4/15.
 */

public class ShowPlanFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ThemeChangedEvent event) {
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
