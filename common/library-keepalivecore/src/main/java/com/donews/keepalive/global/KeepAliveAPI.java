package com.donews.keepalive.global;

import android.app.Application;
import android.app.Notification;

import com.donews.keepalive.Dazzle;
import com.donews.keepalive.DazzleDefaultCallback;
import com.donews.utilslibrary.utils.Utils;
import com.keepalive.daemon.core.DaemonHolder;

/**
 * 入口API类
 *
 * @author Swei
 * @date 2021/4/15 21:03
 * @since v1.0
 */
public class KeepAliveAPI {

    public static void start(Application application, Notification notification){
        DaemonHolder.setGlobalNotifycation(notification);
        DaemonHolder.getInstance().start(application);

        if (Utils.isMainProcess(application)){
            Dazzle.start(application, false, notification, Dazzle.NOTIFICATION_KEEPALIVE, new DazzleDefaultCallback(), new KeepAliveNotificationClick(application));
        }

        KeepAliveGlobalConfig.recordOpen(application);
    }



}
