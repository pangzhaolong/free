package com.donews.main.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.donews.main.BuildConfig;
import com.donews.main.ui.SplashActivity;
import com.donews.base.base.BaseApplication;
import com.donews.common.IModuleInit;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.converter.GsonDiskConverter;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.cookie.CookieManger;
import com.donews.network.model.HttpHeaders;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 应用模块: main
 * <p>
 * 类描述: main组件的业务初始化
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-26
 */
public class MainModuleInit implements IModuleInit {
    private int appCount;
    private long stopTime;

    private Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {


        @Override
        public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {
//            setExcludeFromRecentsActivity();
        }

        @Override
        public void onActivityStarted(@NotNull Activity activity) {
            if (appCount <= 0) {
//                    LogUtil.i("switchad", "app程序进入前台" + appCount);
                toForeGround(activity);
            }
            appCount++;
        }

        @Override
        public void onActivityResumed(@NotNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NotNull Activity activity) {
        }

        @Override
        public void onActivityStopped(@NotNull Activity activity) {
            appCount--;
            if (appCount == 0) {
                stopTime = System.currentTimeMillis();
            }

        }

        @Override
        public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NotNull Activity activity) {

        }
    };

    /**
     * 进入前台
     */
    private void toForeGround(Activity activity) {
        int backGroundInt = SPUtils.getInformain(KeySharePreferences.SPLASH_BACKGROUND_INTERVAL_TIME, 0);
        if (activity instanceof SplashActivity || backGroundInt == 0) {
            return;
        }
        long currentTime = System.currentTimeMillis();

        long seconds = (currentTime - stopTime) / 1000;
        LogUtil.d("toForeGround: seconds:" + seconds);
        if (seconds > backGroundInt) {
            SplashActivity.toForeGround(activity);
//            activity.startActivity(new Intent(activity, SplashActivity.class));
        }
    }

    @Override
    public boolean onInitAhead(BaseApplication application) {
        ScreenAutoAdapter.setup(application);
        EasyHttp.init(application);
        if (LogUtil.allow) {
            EasyHttp.getInstance().debug("HoneyLife", true);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.HEAD_AUTHORIZATION, AppInfo.getToken(""));
        EasyHttp.getInstance()
                .setBaseUrl(BuildConfig.BASE_CONFIG_URL)
                .setReadTimeOut(15 * 1000)
                .setWriteTimeOut(15 * 1000)
                .setConnectTimeout(15 * 1000)
                .setRetryCount(3)
                .setCookieStore(new CookieManger(application))
                .setCacheDiskConverter(new GsonDiskConverter())
                .setCacheMode(CacheMode.FIRSTREMOTE)
                .addCommonHeaders(httpHeaders);

        application.registerActivityLifecycleCallbacks(callbacks);

        ExitInterceptUtils.INSTANCE.init();
        return false;
    }

    @Override
    public boolean onInitLow(BaseApplication application) {
        return false;
    }
}
