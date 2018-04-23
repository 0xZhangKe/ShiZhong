package com.zhangke.shizhong.page.poster.showposter;

import android.content.Context;
import android.text.TextUtils;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.zlog.ZLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 负责通过 UserId 获取海报数据
 * Created by ZhangKe on 2018/4/22.
 */

public class ShowMoviePosterModel implements IShowMoviePosterContract.Model {

    private static final String TAG = "ShowMoviePosterModel";

    private Context context;
    private IShowMoviePosterContract.View showMovieView;
    private String userId;

    private int start = 0;
    private List<MoviePosterBean> listData = new ArrayList<>();

    public ShowMoviePosterModel(Context context,
                                IShowMoviePosterContract.View showMovieView,
                                String userId) {
        this.context = context;
        this.showMovieView = showMovieView;
        this.userId = userId;
    }

    @Override
    public void getMoviePoster() {
        showMovieView.showRoundProgressDialog();
        performMoviePosterRequest();
    }

    private void performMoviePosterRequest(){
        ApiStores apiStores = AppClient.moviePosterRetrofit().create(ApiStores.class);
        Call<ResponseBody> call = apiStores.getMoviePosters(userId, start);
        start += 15;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String responseBody = response.body().string();
                        analysisPosterFromHtml(responseBody);
                    } else {
                        showMovieView.closeRoundProgressDialog();
                        showMovieView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                    }
                } catch (IOException e) {
                    showMovieView.closeRoundProgressDialog();
                    showMovieView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showMovieView.closeRoundProgressDialog();
                showMovieView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.internet_error));
            }
        });
    }

    private void analysisPosterFromHtml(String htmlText) {
        final Pattern movieModelPattern = Pattern.compile("<img alt=(.{1,50}) src=(.{30,100}) class=\"\">");
        Matcher movieModelMatcher = movieModelPattern.matcher(htmlText);
        Pattern imgUrlPattern = Pattern.compile("https://img(.{0,5}).doubanio.com/view/photo/s_ratio_poster/public/(.{1,30}).jpg");
        Pattern namePattern = Pattern.compile("alt=\"(.{1,20})\" ");
        boolean isEmpty = true;
        while (movieModelMatcher.find()) {
            isEmpty = false;
            String movieName = "";
            String fileUrl = "";

            String modelString = movieModelMatcher.group();
            Matcher nameMatcher = namePattern.matcher(modelString);
            while (nameMatcher.find()) {
                String name = nameMatcher.group();
                if (name != null && !name.isEmpty()) {
                    name = name.trim();
                    name = name.replaceAll(" ", "_");
                    name = name.replaceAll("\"", "");
                    name = name.replaceAll("alt=", "");
                    ZLog.i(TAG, "movie name: " + name);
                    movieName = name;
                }
            }
            Matcher urlMatcher = imgUrlPattern.matcher(modelString);
            while (urlMatcher.find()) {
                fileUrl = urlMatcher.group();
            }
            if (!TextUtils.isEmpty(movieName) && !TextUtils.isEmpty(fileUrl))
                listData.add(new MoviePosterBean(movieName, fileUrl));
        }

        if (isEmpty) {
            showMovieView.closeRoundProgressDialog();
            showMovieView.notifyDataChanged(listData);
        } else {
            performMoviePosterRequest();
        }
    }
}
