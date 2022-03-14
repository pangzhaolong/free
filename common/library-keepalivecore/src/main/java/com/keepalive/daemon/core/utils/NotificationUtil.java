package com.keepalive.daemon.core.utils;

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

import com.donews.keepalive.Dazzle;
import com.keepalive.daemon.core.Constants;
import com.keepalive.daemon.core.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtil {
    /**
     *    此次是逆向競爭對手的做法，下次參考
     *    int var2 = var0.getApplicationInfo().icon;
     *         PendingIntent var3 = PendingIntent.getActivity(var0, 10199, var1, 134217728);
     *         NotificationManager var4 = (NotificationManager)var0.getSystemService("notification");
     *         if (VERSION.SDK_INT >= 26) {
     *             try {
     *                 NotificationChannel var10001 = new NotificationChannel("keepalive", "后台服务", 4);
     *                 var10001.setDescription("天气预报");
     *                 var10001.setLockscreenVisibility(-1);
     *                 var10001.enableLights(false);
     *                 var10001.enableLights(false);
     *                 var10001.setShowBadge(false);
     *                 var10001.setBypassDnd(true);
     *                 var10001.setSound((Uri)null, (AudioAttributes)null);
     *                 var4.createNotificationChannel(var10001);
     *             } catch (Exception var6) {
     *                 var6.printStackTrace();
     *             }
     *         }
     *
     *         Builder var5;
     *         if (VERSION.SDK_INT >= 26) {
     *             var5 = new Builder.<init>(var0, "keepalive");
     *         } else {
     *             var5 = new Builder.<init>(var0);
     *         }
     *
     *         var5.setSmallIcon(var2);
     *         var5.setContentTitle("垃圾清理");
     *         var5.setFullScreenIntent(var3, true);
     *         if (VERSION.SDK_INT >= 21) {
     *             var5.setVisibility(-1);
     *         }
     *
     *         PendingIntent var10000 = var3;
     *         var4.cancel("AA_TAG1_CHARGE_SCREEN", 10103);
     *         Notification var10 = var5.getNotification();
     *         var4.notify("AA_TAG1_CHARGE_SCREEN", 10103, var10);
     *         (new Handler(Looper.getMainLooper())).postDelayed(new a(var0), 100L);
     *
     *         try {
     *             var10000.send();
     *         } catch (Exception var9) {
     *             Context var11;
     *             boolean var12;
     *             Intent var13;
     *             try {
     *                 var11 = var0;
     *                 var13 = var1;
     *                 var1.addFlags(268435456);
     *             } catch (Exception var8) {
     *                 var12 = false;
     *                 return;
     *             }
     *
     *             try {
     *                 var11.startActivity(var13);
     *             } catch (Exception var7) {
     *                 var12 = false;
     *             }
     *         }
     *
     */

    /**
     * 唯一前台通知ID
     */
    public static final int NOTIFICATION_ID = Dazzle.NOTIFICATION_KEEPALIVE;

    public static Notification createDefaultNotification(Context context){
        return NotificationUtil.createNotification(
                context,
                R.drawable.ic_launcher_round,
                R.drawable.ic_launcher_round,
                "",
                "",
                true,
                NotificationCompat.PRIORITY_DEFAULT,
                NotificationManager.IMPORTANCE_DEFAULT,
                Constants.NOTI_TICKER_TEXT,
                null,
                null
        );
    }

    public static Notification createNotification(Context context,
                                                  int smallIconId,
                                                  int largeIconId,
                                                  String title,
                                                  String text,
                                                  boolean ongoing,
                                                  int pri,
                                                  int importance,
                                                  CharSequence tickerText,
                                                  PendingIntent pendingIntent,
                                                  RemoteViews views) {
        Logger.d(Logger.TAG, "call createNotification(): smallIconId=" + smallIconId
                + ", largeIconId=" + largeIconId + ", title=" + title + ", text=" + text
                + ", ongoing=" + ongoing + ", pri=" + pri + ", tickerText=" + tickerText
                + ", pendingIntent=" + pendingIntent + ", remoteViews=" + views);
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String channelId = context.getPackageName() + ".notification.channelId";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 用户可见的通道名称
            String channelName = context.getPackageName() + ".notification.channelName";
            // 通道的重要程度
            if (importance < 0 || importance > 5) {
                importance = NotificationManager.IMPORTANCE_DEFAULT;
            }
            NotificationChannel nc = new NotificationChannel(channelId, channelName, importance);
            nc.setDescription(context.getPackageName() + ".notification.description");
            if (nm != null) {
                nm.createNotificationChannel(nc);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        // 设置通知小图标
        if (smallIconId == 0) {
            builder.setSmallIcon(R.drawable.noti_icon);
//            Logger.w(Logger.TAG, "Oops!!! Invalid notification small smallIconId.");
//            return null;
        } else {
            builder.setSmallIcon(smallIconId);
        }

        // 设置通知大图标
        if (largeIconId > 0) {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), largeIconId);
            builder.setLargeIcon(bm);
        }

        // 设置通知标题
        String label = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        if (TextUtils.isEmpty(title)) {
            builder.setContentTitle(label);
        } else {
            builder.setContentTitle(title);
        }

        // 设置通知内容
        if (TextUtils.isEmpty(text)) {
            builder.setContentText(label + "正在运行");
        } else {
            builder.setContentText(text);
        }

        // 设置通知显示的时间
        builder.setWhen(System.currentTimeMillis());

        // 设置是否常驻
        builder.setOngoing(ongoing);

        // 设置优先级
        if (pri >= NotificationCompat.PRIORITY_MIN && pri <= NotificationCompat.PRIORITY_MAX) {
            builder.setPriority(pri);
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        // 设置提示
        if (!TextUtils.isEmpty(tickerText)) {
            builder.setTicker(tickerText);
        }

        // 设置 ContentIntent
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }

        // 设置自定义布局
        if (views != null) {
            builder.setContent(views);
        }

        // 创建通知并返回
        return builder.build();
    }
    public static void showNotification(Service service, Notification notification) {
        showNotification(service,notification,NOTIFICATION_ID);
    }

    public static void showNotification(Service service, Notification notification,int notifacationID) {
        if (notification == null) {
            return;
        }
        try {
            service.startForeground(notifacationID, notification);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
