package com.zhangke.shizhong.page.poster.inputname;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户数据实体
 * <p>
 * Created by ZhangKe on 2018/4/17.
 */

public class UserBean {

    private static final String TAG = "SearchUserBean";

    private static Pattern doubanUserIconPattern = Pattern.compile("https://img(.{15,40}).jpg");
    private static Pattern doubanNickNamePattern = Pattern.compile("alt=\"(.{1,20})\"");
    private static Pattern doubanUserIdPattern = Pattern.compile("sid:(.{3,13}),");
    private static Pattern doubanDescriptionPattern = Pattern.compile("info\">(.{5,14})<");


    private String userIcon;
    private String nickName;
    private String userId;
    private String description;

    public UserBean() {
    }

    public UserBean(String html) {
        html = html.replaceAll("\n", "");
        html = html.replaceAll(" ", "");

        Matcher matcher = doubanUserIconPattern.matcher(html);
        while (matcher.find()) {
            userIcon = matcher.group();
        }

        matcher = doubanNickNamePattern.matcher(html);
        while (matcher.find()) {
            nickName = matcher.group();
            if(!TextUtils.isEmpty(nickName) && nickName.length() > 5){
                try {
                    nickName = nickName.substring(5, nickName.length() - 1);
                }catch(Exception e){
                    Log.e(TAG, "UserBean: ", e);
                }
            }
        }

        matcher = doubanUserIdPattern.matcher(html);
        while (matcher.find()) {
            userId = matcher.group();
            if(!TextUtils.isEmpty(userId) && userId.length() > 4){
                try {
                    userId = userId.substring(4, userId.length() - 1);
                }catch(Exception e){
                    Log.e(TAG, "UserBean: ", e);
                }
            }
        }

        matcher = doubanDescriptionPattern.matcher(html);
        while (matcher.find()) {
            description = matcher.group();
            if(!TextUtils.isEmpty(description) && description.length() > 6){
                try {
                    description = description.substring(6, description.length() - 1);
                }catch(Exception e){
                    Log.e(TAG, "UserBean: ", e);
                }
            }
        }
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
