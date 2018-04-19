package com.zhangke.shizhong.page.poster.inputName;

import android.content.Context;

/**
 * 搜索姓名界面Presenter实现类
 * <p>
 * Created by ZhangKe on 2018/4/17.
 */

public class InputNamePresenter implements IInputNameContract.Presenter {

    private static final String TAG = "InputNamePresenter";

    private Context context;
    private IInputNameContract.View inputNameView;

    private IInputNameContract.Model inputNameModel;

    /**
     * 0-豆瓣电影海报
     * 1-云音乐封面
     */
    private int type;

    private String searchName;

    InputNamePresenter(Context context, IInputNameContract.View inputNameView, int type) {
        this.context = context;
        this.inputNameView = inputNameView;
        this.type = type;

        inputNameModel = new InputNameModel();
    }

    @Override
    public void searchUserFromName(String name) {
        this.searchName = name;
        inputNameView.setButtonLoading(true);
        inputNameModel.reset();
        if (type == 0) {
            getDoubanUsers(true);
        } else {
            get163MusicUsers();
        }
    }

    @Override
    public void clearUsers() {
        inputNameModel.reset();
    }

    @Override
    public void loadMore() {
        getDoubanUsers(false);
    }

    private void getDoubanUsers(boolean firstPage) {
        inputNameModel.getDoubanUsers(searchName,
                response -> {
                    if (firstPage) {
                        inputNameView.showNameList();
                        inputNameView.setButtonLoading(false);
                    } else {
                        inputNameView.closeLoadMoreView();
                    }
                    inputNameView.notifyUserListChanged(response);
                },
                cause -> {
                    if (firstPage) {
                        inputNameView.setButtonLoading(false);
                    } else {
                        inputNameView.closeLoadMoreView();
                    }
                    inputNameView.showNoActionSnackbar(cause);
                });
    }

    private void get163MusicUsers() {

    }
}
