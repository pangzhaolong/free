package com.donews.common.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.donews.common.R;

/**
 * @author by SnowDragon
 * Date on 2021/2/23
 * Description:
 */
public class NotificationUtil {
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID_SUFFIX = ".do_news";
    private Context mContext;

    private Notification notification;

    /**
     * 和保活Id保持一致，公用一个通知
     */
    public static final int NOTIFICATION_ID = 0x9999;


    public NotificationUtil(Context mContext) {
        this.mContext = mContext;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    /**
     * 展示通知栏
     */
    public void showCustomNotification(String desc, String awardContent) {
        if (mContext == null) {
            return;
        }
        String channelId = mContext.getPackageName() + CHANNEL_ID_SUFFIX;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, mContext.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("通知栏");
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);
            notification = new NotificationCompat.Builder(mContext, channelId)
                    .setSmallIcon(R.drawable.noti_icon)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
//                    .setCustomBigContentView(getContentView(desc,awardContent))
//                    .setCustomContentView(getContentView(desc,awardContent))
                    .setContent(getContentView(desc, awardContent))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("")
                    .setOngoing(true)
                    .setChannelId(mChannel.getId())
                    .build();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            notification = new NotificationCompat.Builder(mContext, channelId)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.noti_icon)
                    .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
//                    .setCustomBigContentView(getContentView(desc,awardContent))
//                    .setCustomContentView(getContentView(desc, awardContent))
                    .setContent(getContentView(desc, awardContent))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("")
                    .setOngoing(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(mContext, channelId)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.noti_icon)
                    .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
                    .setContent(getContentView(desc, awardContent))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("")
                    .setOngoing(true)
                    .build();
        }


        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private PendingIntent getDefaultIntent(int flags) {
        return PendingIntent.getActivity(mContext, 1, new Intent(), flags);
    }

    /**
     * 获取自定义通知栏view
     *
     * @param desc         描述
     * @param awardContent 奖励内容
     * @return
     */
    private RemoteViews getContentView(String desc, String awardContent) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.common_view_notify_award);
        mView.setTextViewText(R.id.tv_desc, desc);
        mView.setTextViewText(R.id.tv_award_content, awardContent);

        return mView;
    }


}
