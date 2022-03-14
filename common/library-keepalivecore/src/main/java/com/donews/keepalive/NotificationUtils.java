
package com.donews.keepalive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.keepalive.daemon.core.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {
    public static final String TAG = NotificationUtils.class.getSimpleName();

    public static final String id = "channel_1";
    public static final int ID = 101;
    public static final String name = "notification";
    private NotificationManager manager;
    private Context mContext;

    public NotificationUtils(Context base) {
        mContext = base;
    }

    @RequiresApi(api = 26)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(null, null);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void sendNotificationFullScreen(String title, String content, Intent fullScreenIntent) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotificationQ
                    (title, content, fullScreenIntent);
            getManager().notify(ID, notification);
        }
    }

    public static void clearAllNotification(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(ID);
        } catch (Throwable t) {
        }
    }

    public Notification getChannelNotificationQ(String title, String content, Intent fullScreenIntent) {
//        clearAllNotification(mContext);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(mContext, 0, fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, id);
//        notificationBuilder.setSmallIcon(R.drawable.icon_phone);
        notificationBuilder.setSmallIcon(R.drawable.ts_notification);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setTicker(content);
        notificationBuilder.setContentText(content);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        notificationBuilder.setCategory(Notification.CATEGORY_CALL);
        notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification_tr);
        notificationBuilder.setCustomBigContentView(remoteViews);
        notificationBuilder.setCustomContentView(remoteViews);
        return notificationBuilder.build();
    }
}
