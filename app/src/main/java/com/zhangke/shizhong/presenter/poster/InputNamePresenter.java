package com.zhangke.shizhong.presenter.poster;

import android.content.Context;

import com.zhangke.shizhong.contract.poster.IInputNameContract;
import com.zhangke.shizhong.model.poster.InputNameModel;

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

    public InputNamePresenter(Context context, IInputNameContract.View inputNameView, int type) {
        this.context = context;
        this.inputNameView = inputNameView;
        this.type = type;

        inputNameModel = new InputNameModel();
    }

    @Override
    public void searchUserFromName(String name) {
        this.searchName = name;
        inputNameModel.reset();
        if (type == 0) {
            getDoubanUsers(true);
        } else {
            get163MusicUsers(true);
        }
    }

    @Override
    public void clearUsers() {
        inputNameModel.reset();
    }

    @Override
    public void loadMore() {
        if (type == 0) {
            getDoubanUsers(false);
        }else{
            get163MusicUsers(false);
        }
    }

    private void getDoubanUsers(boolean firstPage) {
        if(firstPage){
            inputNameView.showRoundProgressDialog();
        }
        inputNameModel.getDoubanUsers(searchName,
                response -> {
                    if (firstPage) {
                        inputNameView.closeRoundProgressDialog();
                    } else {
                        inputNameView.closeLoadMoreView();
                    }
                    inputNameView.notifyUserListChanged(response);
                },
                cause -> {
                    if (firstPage) {
                        inputNameView.closeRoundProgressDialog();
                    } else {
                        inputNameView.closeLoadMoreView();
                    }
                    inputNameView.showNoActionSnackbar(cause);
                });
    }

    private void get163MusicUsers(boolean firstPage) {
        if(firstPage){
            inputNameView.showRoundProgressDialog();
        }
        inputNameModel.get163MusicUsers(searchName,
                response -> {
                    if (firstPage) {
                        inputNameView.closeRoundProgressDialog();
                    } else {
                        inputNameView.closeLoadMoreView();
                    }
                    inputNameView.notifyUserListChanged(response);
                },
                cause -> {
                    if (firstPage) {
                        inputNameView.closeRoundProgressDialog();
                    } else {
                        inputNameView.closeLoadMoreView();
                    }
                    inputNameView.showNoActionSnackbar(cause);
                });
    }
}
