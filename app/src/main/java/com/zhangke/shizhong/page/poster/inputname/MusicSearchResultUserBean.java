package com.zhangke.shizhong.page.poster.inputname;

import java.util.List;

/**
 * 云音乐用户接口数据
 * Created by ZhangKe on 2018/4/23.
 */

public class MusicSearchResultUserBean {

    private ResultBean result;
    private int code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResultBean {

        private int userprofileCount;
        private List<UserprofilesBean> userprofiles;

        public int getUserprofileCount() {
            return userprofileCount;
        }

        public void setUserprofileCount(int userprofileCount) {
            this.userprofileCount = userprofileCount;
        }

        public List<UserprofilesBean> getUserprofiles() {
            return userprofiles;
        }

        public void setUserprofiles(List<UserprofilesBean> userprofiles) {
            this.userprofiles = userprofiles;
        }

        public static class UserprofilesBean {

            private String avatarUrl;
            private int userId;
            private int userType;
            private String nickname;
            private String signature;
            private String description;
            private String detailDescription;
            private String backgroundUrl;
            private int followeds;
            private int follows;

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDetailDescription() {
                return detailDescription;
            }

            public void setDetailDescription(String detailDescription) {
                this.detailDescription = detailDescription;
            }

            public String getBackgroundUrl() {
                return backgroundUrl;
            }

            public void setBackgroundUrl(String backgroundUrl) {
                this.backgroundUrl = backgroundUrl;
            }

            public int getFolloweds() {
                return followeds;
            }

            public void setFolloweds(int followeds) {
                this.followeds = followeds;
            }

            public int getFollows() {
                return follows;
            }

            public void setFollows(int follows) {
                this.follows = follows;
            }
        }
    }
}
