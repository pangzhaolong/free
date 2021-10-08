package com.dn.sdk.lib.donews;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dn.sdk.BuildConfig;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.utils.SdkLogUtils;
import com.dn.sdk.widget.AdVideoCloseView;

/**
 * @author by SnowDragon
 * Date on 2020/12/18
 * Description:
 */
public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLifecycleListener";

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Class<?> cls = activity.getClass();
            SdkLogUtils.i(SdkLogUtils.TAG, " onActivityCreated " + cls.getCanonicalName());
        }
        ACache.getInstance().saveTopActivity(activity);
        AdVideoCloseView.onAddView(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    private static final String MAIN_ACTIVITY_NAME = "MainActivity";

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SdkLogUtils.w(SdkLogUtils.TAG, "destroyedActivity: " + activity.getClass().getSimpleName());
        //如果MainActivity被销毁，清空已经缓存的视频
        if (MAIN_ACTIVITY_NAME.equals(activity.getClass().getSimpleName())) {
            ACache.getInstance().cleanInvalidCache();
        }

    }
}
