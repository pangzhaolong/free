package com.keepalive.daemon.core;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.donews.base.base.AppManager;
import com.donews.keepalive.global.KeepAliveGlobalConfig;
import com.keepalive.daemon.core.component.DaemonInstrumentation;
import com.keepalive.daemon.core.component.DaemonReceiver;
import com.keepalive.daemon.core.component.DaemonService;
import com.keepalive.daemon.core.notification.NotifyResidentService;
import com.keepalive.daemon.core.utils.Logger;
import com.keepalive.daemon.core.utils.ServiceHolder;

import java.util.HashMap;
import java.util.Map;

public class DaemonHolder {

    //公共的notification通知
    private Notification notification;

    public static Map<Activity, ServiceConnection> connCache = new HashMap<>();

    private android.os.Handler mHandler;


    private DaemonHolder() {
    }

    private static class Holder {
        private volatile static DaemonHolder INSTANCE = new DaemonHolder();
    }

    public static DaemonHolder getInstance() {
        return Holder.INSTANCE;
    }

    public android.os.Handler getHandler(){
        if(mHandler == null){
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public static void setGlobalNotifycation(Notification notifycation){
        getInstance().notification = notifycation;
    }

    public static Notification getGlobalNotifycation(){
        return getInstance().notification;
    }

    public void start(final Application app){
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                Logger.v(Logger.TAG, String.format("====> [%s] created", activity.getLocalClassName()));
                try {
                    ServiceHolder.getInstance().bindService(activity, DaemonService.class,
                            (connection, isConnected) -> {
                                if (isConnected) {
                                    connCache.put(activity, connection);
                                }
                            });
                } catch (Exception e) {

                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Logger.v(Logger.TAG, String.format("====> [%s] started", activity.getLocalClassName()));
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Logger.v(Logger.TAG, String.format("====> [%s] resumed", activity.getLocalClassName()));
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Logger.v(Logger.TAG, String.format("====> [%s] paused", activity.getLocalClassName()));
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Logger.v(Logger.TAG, String.format("====> [%s] stopped", activity.getLocalClassName()));
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Logger.v(Logger.TAG, String.format("====> [%s] save instance state", activity.getLocalClassName()));
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Logger.v(Logger.TAG, String.format("====> [%s] destroyed", activity.getLocalClassName()));
                if (connCache.containsKey(activity)) {
                    ServiceHolder.getInstance().unbindService(activity, connCache.get(activity));
                }
            }
        });

        //如果是第一次启动
        boolean isFirstOpen = KeepAliveGlobalConfig.isFirstOpen(app);
        if(isFirstOpen){
            getHandler().postDelayed(() -> {
                attach(app);
            },KeepAliveGlobalConfig.getDelayOpenTime());
        }else{
            attach(app);
        }
    }

    public void attach(Application app) {
        JavaDaemon.getInstance().fire(
                app,
                new Intent(app, DaemonService.class),
                new Intent(app, DaemonReceiver.class),
                new Intent(app, DaemonInstrumentation.class)
        );

        KeepAliveConfigs configs = new KeepAliveConfigs(
                new KeepAliveConfigs.Config(app.getPackageName() + ":resident",
                        NotifyResidentService.class.getCanonicalName()));
//        configs.ignoreBatteryOptimization();
//        configs.rebootThreshold(10 * 1000, 3);
        configs.setOnBootReceivedListener(new KeepAliveConfigs.OnBootReceivedListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Logger.d(Logger.TAG, "############################# onReceive(): intent=" + intent);
                ServiceHolder.fireService(context, DaemonService.class, false);
            }
        });
        KeepAlive.init(app, configs);
    }
}
