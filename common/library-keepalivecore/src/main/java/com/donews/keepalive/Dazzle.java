package com.donews.keepalive;

import android.app.Application;
import android.app.Notification;

import com.donews.keepalive.global.KeepAliveGlobalConfig;
import com.keepalive.daemon.core.DaemonHolder;

public class Dazzle {
    //Swei脑中第一个想起的数字
    public static final int  NOTIFICATION_KEEPALIVE = 3164;

    public static void start(final Application context, Boolean debug, Notification notification, int id, DazzleCallback callback,ForegroundNotificationClickListener listener){
        DaemonHolder.getInstance().getHandler().postDelayed(() -> {
            init(context,debug,notification,id,callback,listener);
        },KeepAliveGlobalConfig.getMyDelayOpenTime());
    }

    public static void init(Application context, Boolean debug, Notification notification, int id, DazzleCallback callback,ForegroundNotificationClickListener listener) {
        DazzleReal.init(context, debug, notification, id, new RealCallback() {
            @Override
            public void onWorking() {
                callback.onWorking();
            }

            @Override
            public void onStop() {
                callback.onStop();
            }

            @Override
            public void doReport(String type, int pid, long usageTime, long intervalTime) {
                callback.doReport(type, pid, usageTime, intervalTime);
            }
        }, listener);
    }

}
