package com.donews.notify.launcher.notifybar.listeners;

import android.app.Notification;

import androidx.core.app.NotificationCompat;

/**
 * @author lcl
 * Date on 2022/3/11
 * Description:
 */
public interface OnNotificationBuiderCreateListener {
    /**
     * 通过V4兼容方式创建的Builder
     *
     * @param builder
     */
    void builderCompatCreate(NotificationCompat.Builder builder);

    /**
     * 通过正常的app下的Buidler构建的对象
     *
     * @param builder
     */
    void builderCreate(Notification.Builder builder);
}
