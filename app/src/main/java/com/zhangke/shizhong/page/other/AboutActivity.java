package com.zhangke.shizhong.page.other;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangke.shizhong.BuildConfig;
import com.zhangke.shizhong.R;

import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;
import me.drakeet.support.about.OnRecommendedClickedListener;
import me.drakeet.support.about.Recommended;
import me.drakeet.support.about.extension.RecommendedLoaderDelegate;
import me.drakeet.support.about.provided.PicassoImageLoader;

/**
 * Created by ZhangKe on 2018/8/1.
 */
@SuppressLint("SetTextI18n")
@SuppressWarnings("SpellCheckingInspection")
public class AboutActivity extends AbsAboutActivity implements OnRecommendedClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImageLoader(new PicassoImageLoader());
        setOnRecommendedClickedListener(this);
    }

    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.round_logo);
        slogan.setText("不忘初心，方得始终。");
        version.setText("v " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull Items items) {
        RecommendedLoaderDelegate.attach(this, items.size());

        items.add(new Category("About"));
        items.add(new Card(getString(R.string.introduce)));

        items.add(new Category("Developers"));
        items.add(new Contributor(R.mipmap.mine_icon, "张可", "Developer & Designer", "https://github.com/0xZhangKe"));

        items.add(new Category("Open Source Licenses"));
        items.add(new License("ShiZhong", "ZhangKe", License.APACHE_2, "https://github.com/0xZhangKe/ShiZhong"));
    }

    @Override
    public boolean onRecommendedClicked(@NonNull View itemView, @NonNull Recommended recommended) {
        if(recommended.openWithGooglePlay) {
            openMarket(this, recommended.packageName, recommended.downloadUrl);
        } else {
            openWithBrowser(this, recommended.downloadUrl);
        }
        return false;
    }

    private void openMarket(@NonNull Context context, @NonNull String targetPackage, @NonNull String defaultDownloadUrl) {
        try {
            Intent googlePlayIntent = context.getPackageManager().getLaunchIntentForPackage("com.android.vending");
            ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity");
            // noinspection ConstantConditions
            googlePlayIntent.setComponent(comp);
            googlePlayIntent.setData(Uri.parse("market://details?id=" + targetPackage));
            context.startActivity(googlePlayIntent);
        } catch (Throwable e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(defaultDownloadUrl)));
            e.printStackTrace();
        }
    }

    public static void openWithBrowser(Context context, String url) {
        //在浏览器打开
        try {
            context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), "在浏览器中打开"));
        } catch (ActivityNotFoundException e) {
            // nop
        }
    }

}