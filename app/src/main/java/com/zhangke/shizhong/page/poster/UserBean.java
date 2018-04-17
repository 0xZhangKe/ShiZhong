package com.zhangke.shizhong.page.poster;

/**
 * 用户数据实体
 * <p>
 * Created by ZhangKe on 2018/4/17.
 */

public class UserBean {

    private static final String TAG = "SearchUserBean";

    private String userIcon;
    private String nickName;
    private String userId;
    private String description;

    public UserBean() {
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
