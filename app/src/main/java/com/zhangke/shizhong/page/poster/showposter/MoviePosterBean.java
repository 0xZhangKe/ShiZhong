package com.zhangke.shizhong.page.poster.showposter;

/**
 * 电影海报数据实体
 * Created by ZhangKe on 2018/4/22.
 */

public class MoviePosterBean {

    private String movieName;
    private String movieImageUrl;

    public MoviePosterBean() {
    }

    public MoviePosterBean(String movieName, String movieImageUrl) {
        this.movieName = movieName;
        this.movieImageUrl = movieImageUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }
}
