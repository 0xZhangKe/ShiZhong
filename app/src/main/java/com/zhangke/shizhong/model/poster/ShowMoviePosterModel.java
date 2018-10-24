package com.zhangke.shizhong.model.poster;

import android.content.Context;
import android.text.TextUtils;

import com.zhangke.shizhong.R;
import com.zhangke.shizhong.common.ApiStores;
import com.zhangke.shizhong.common.AppClient;
import com.zhangke.shizhong.common.SZApplication;
import com.zhangke.shizhong.contract.poster.IShowMoviePosterContract;
import com.zhangke.shizhong.util.HttpObserver;
import com.zhangke.zlog.ZLog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
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
    private ApiStores apiStores = AppClient.moviePosterRetrofit().create(ApiStores.class);

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

    private void performMoviePosterRequest() {
        apiStores.getMoviePosters(userId, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpObserver<Response<ResponseBody>>() {
                    @Override
                    protected void onErrorResponse(String errorMessage) {
                        showMovieView.closeRoundProgressDialog();
                        showMovieView.showNoActionSnackbar(errorMessage);
                    }

                    @Override
                    protected void onSuccessResponse(Response<ResponseBody> response) {
                        try {
                            if (response.body() != null
                                    && !TextUtils.isEmpty(response.toString())) {
                                analysisPosterFromHtml(response.toString());
                                start += 15;
                            } else {
                                showMovieView.closeRoundProgressDialog();
                                showMovieView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                            }
                        } catch (Exception e) {
                            showMovieView.closeRoundProgressDialog();
                            showMovieView.showNoActionSnackbar(SZApplication.getInstance().getString(R.string.data_error));
                        }
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
