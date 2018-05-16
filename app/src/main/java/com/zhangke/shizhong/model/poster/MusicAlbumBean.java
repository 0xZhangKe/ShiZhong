package com.zhangke.shizhong.model.poster;

import com.zhangke.shizhong.widget.MultiItemLayoutManger;

import java.io.Serializable;
import java.util.List;

/**
 * 专辑数据实体
 * <p>
 * Created by ZhangKe on 2018/4/23.
 */

public class MusicAlbumBean implements Serializable{

    static final long serialVersionUID = 42L;

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

    public static class ResultBean implements Serializable{

        static final long serialVersionUID = 42L;

        private int playlistCount;
        private List<PlaylistsBean> playlists;

        public int getPlaylistCount() {
            return playlistCount;
        }

        public void setPlaylistCount(int playlistCount) {
            this.playlistCount = playlistCount;
        }

        public List<PlaylistsBean> getPlaylists() {
            return playlists;
        }

        public void setPlaylists(List<PlaylistsBean> playlists) {
            this.playlists = playlists;
        }

        public static class PlaylistsBean implements Serializable{

            static final long serialVersionUID = 42L;

            private String id;
            private String name;
            private String coverImgUrl;
            private int trackCount;//歌曲数量
            private int userId;
            private int playCount;//播放次数

            private int itemType = MultiItemLayoutManger.MENU_ITEM_TYPE;
            private String userIcon;//用户头像， title 使用
            private String description;//用户描述

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCoverImgUrl() {
                return coverImgUrl;
            }

            public void setCoverImgUrl(String coverImgUrl) {
                this.coverImgUrl = coverImgUrl;
            }

            public int getTrackCount() {
                return trackCount;
            }

            public void setTrackCount(int trackCount) {
                this.trackCount = trackCount;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getPlayCount() {
                return playCount;
            }

            public void setPlayCount(int playCount) {
                this.playCount = playCount;
            }

            public String getUserIcon() {
                return userIcon;
            }

            public void setUserIcon(String userIcon) {
                this.userIcon = userIcon;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }
        }
    }
}
