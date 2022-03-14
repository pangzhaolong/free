package com.donews.notify.launcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.donews.keepalive.NotificationClickReceiver;

/**
 * 后续深入研究notification api，现在暂时这样吧
 */
public class NotificationCreate {


    public static Notification createNotification(Context context, String channelId,
                                                  String appName, int drawableId,
                                                  String title, String text) {
        try {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, NotificationClickReceiver.class);
            intent.setAction(NotificationClickReceiver.ACTION_CLICK_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationApiCompat.Builder(context, nm, channelId, appName, drawableId)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)
                    .setCategory()
                    .setTicker(appName)
                    .setOngoing(true)
                    .setPriority(Notification.PRIORITY_MIN)
                    .setColor()
                    .setOnlyAlertOnce(true)
                    .initRemoteViews()
                    .builder().notificationApiCompat;

            return notification;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }


}
