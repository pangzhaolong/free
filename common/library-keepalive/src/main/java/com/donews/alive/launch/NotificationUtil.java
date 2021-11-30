package com.donews.alive.launch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.donews.alive.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author by SnowDragon
 * Date on 2020/12/4
 * Description:
 */
public class NotificationUtil {

    /**
     * 唯一前台通知ID
     */
    public static final int NOTIFICATION_ID = 0x666;

    public static final String CHANNEL_ID = "notification.channelId";


    NotificationCompat.Builder notificationBuilder;
    private Notification notification;
    NotificationManager nm;
    Context context;

    private int smallIconId;
    private int largeIconId;
    private String title;
    private String description;
    private boolean isOnGoing = false;
    private RemoteViews views;
    private PendingIntent pendingIntent;


    public NotificationUtil(Context context) {
        this.context = context;
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // 唯一的通知通道的id.
        String channelId = context.getPackageName() + CHANNEL_ID;

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 用户可见的通道名称
            String channelName = context.getPackageName() + CHANNEL_ID;
            // 通道的重要程度
            NotificationChannel nc = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            nc.setDescription(context.getPackageName() + ".notification.description");
            if (nm != null) {
                nm.createNotificationChannel(nc);
            }
        }

        notificationBuilder = new NotificationCompat.Builder(context, channelId);


    }

    /**
     * @param smallIconId 图片resId
     * @return
     */
    public NotificationUtil setSmallIconId(int smallIconId) {
        this.smallIconId = smallIconId;
        return this;
    }

    /**
     * 设置通知大图标
     *
     * @param largeIconId 图片resId
     * @return
     */
    public NotificationUtil setLargeIconId(int largeIconId) {
        this.largeIconId = largeIconId;
        return this;
    }

    public NotificationUtil setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationUtil setDescription(String description) {
        this.description = description;
        return this;
    }

    public NotificationUtil isOngoing(boolean ongoing) {
        isOnGoing = ongoing;
        return this;
    }

    public NotificationUtil setPendingIntent(PendingIntent pendingIntent, boolean isFullIntent) {
        if (isFullIntent) {
            notificationBuilder.setFullScreenIntent(pendingIntent, true);
        } else {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        return this;
    }

    public NotificationUtil setView(RemoteViews view) {
        notificationBuilder.setCustomContentView(view);
        return this;
    }


    public NotificationUtil createNotification() {
        if (smallIconId == 0) {

            smallIconId = (R.drawable.noti_icon);
        }
        Bitmap bm = null;
        if (largeIconId > 0) {
            bm = BitmapFactory.decodeResource(context.getResources(), largeIconId);
            notificationBuilder.setLargeIcon(bm);
        }

        // 设置通知标题
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        if (TextUtils.isEmpty(title)) {
            title = appName;
        }

        // 设置通知内容
        if (TextUtils.isEmpty(description)) {
            description = appName + "正在运行";
        }

        notificationBuilder.setSmallIcon(smallIconId)
                .setLargeIcon(bm)
                //设置权限优先级
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //设置显示时间
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(description)
                .setOngoing(isOnGoing);


        notification = notificationBuilder.build();
        return this;
    }

    public void sendNotification() {
        if (notificationBuilder != null) {
            nm.notify(NOTIFICATION_ID, notification);
        }
    }


    public void clearAllNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    public void showNotification(Service service, Notification notification) {
        if (notification == null) {
            return;
        }

        try {
            service.startForeground(NOTIFICATION_ID, notification);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
