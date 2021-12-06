package com.donews.notify.launcher;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.donews.notify.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class NotificationApiCompat {
    public static final String ROM_VIVO = "VIVO";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private final NotificationManager manager;

    private final Notification.Builder mBuilder26;
    private final NotificationCompat.Builder mBuilder25;
    private Builder builder;

    public Notification notificationApiCompat;
    public static RemoteViews remoteViews = null;
    public static RemoteViews remoteViews2 = null;
    public static RemoteViews remoteViews3 = null;

    private static String sName;
    private static String sVersion;
    public NotificationApiCompat(Builder builder) {
        manager = builder.manager;
        notificationApiCompat = builder.mNotification;
        mBuilder26 = builder.mBuilder26;
        mBuilder25 = builder.mBuilder25;
    }


    public void notify(int id) {
        manager.notify(id, notificationApiCompat);
    }
    public static boolean isVivo() {
        return check(ROM_VIVO);
    }
    public static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        }
        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
            return true;
        }else {
            return false;
        }
    }
    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }


    public static class Builder {
        private Context mContext;
        private NotificationManager manager;
        private String mChannelId;
        private String channelName;
        private int smallIcon;

        Notification mNotification = null;
        private NotificationChannel mNotificationChannel = null;
        Notification.Builder mBuilder26 = null;
        NotificationCompat.Builder mBuilder25 = null;

        public Builder(Context mContext, NotificationManager manager, String mChannelId, String channelName, int smallIcon) {
            this.mContext = mContext;
            this.manager = manager;
            this.mChannelId = mChannelId;
            this.channelName = channelName;
            this.smallIcon = smallIcon;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel = new NotificationChannel(mChannelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                mBuilder26 = getChannelNotification(mContext, mChannelId);
                mBuilder26.setSmallIcon(smallIcon);
            } else {
                mBuilder25 = getNotification_25(mContext);
                mBuilder25.setSmallIcon(smallIcon);
            }
        }

        public Builder setPriority(int pri) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setPriority(pri);
            } else {
                mBuilder25.setPriority(pri);
            }
            return this;
        }


        public Builder setContentIntent(PendingIntent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setContentIntent(intent);
            } else {
                mBuilder25.setContentIntent(intent);
            }


            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setTicker(tickerText);
            } else {
                mBuilder25.setTicker(tickerText);
            }
            return this;
        }

        public Builder setColor() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setColor(mContext.getResources().getColor(R.color.teal_200));
            } else {
                mBuilder25.setColor(mContext.getResources().getColor(R.color.teal_200));
            }
            return this;
        }

        Builder initRemoteViews() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notify_remote_view);
                remoteViews2 = new RemoteViews(mContext.getPackageName(), R.layout.notify_remote_view);
                remoteViews.setImageViewResource(R.id.img_notify,R.drawable.notifycation_notify_content);
                remoteViews2.setImageViewResource(R.id.img_notify,R.drawable.notifycation_notify_content);
                mBuilder26.setCustomBigContentView(remoteViews);
                mBuilder26.setCustomContentView(remoteViews2);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!isVivo()) {
                    remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notify_remote_view);
                    remoteViews.setImageViewResource(R.id.img_notify,R.drawable.notifycation_notify_content);
                    mBuilder25.setCustomBigContentView(remoteViews);
                }
                remoteViews2 = new RemoteViews(mContext.getPackageName(), R.layout.notify_remote_view);
                remoteViews2.setImageViewResource(R.id.img_notify,R.drawable.notifycation_notify_content);
                mBuilder25.setCustomContentView(remoteViews2);
            } else {
                remoteViews3 = new RemoteViews(mContext.getPackageName(), R.layout.notify_remote_view);
                remoteViews3.setImageViewResource(R.id.img_notify,R.drawable.notifycation_notify_content);
                mBuilder25.setContent(remoteViews3);
            }
            return this;
        }

        Builder setStyle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setStyle(new Notification.MediaStyle());
            } else {
                mBuilder25.setStyle(new NotificationCompat.BigPictureStyle());
            }
            return this;
        }

        public Builder setCategory() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setCategory(Notification.CATEGORY_SERVICE);
            } else {
                mBuilder25.setCategory(Notification.CATEGORY_SERVICE);
            }
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setContentTitle(title);
            } else {
                mBuilder25.setContentTitle(title);
            }
            return this;
        }

        public Builder setContentText(CharSequence text) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setContentText(text);
            } else {
                mBuilder25.setContentText(text);
            }
            return this;
        }

        public Builder setOngoing(Boolean ongoing) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setOngoing(ongoing);
            } else {
                mBuilder25.setOngoing(ongoing);
            }
            return this;
        }

        public Builder setOnlyAlertOnce(Boolean onlyAlertOnce) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setOnlyAlertOnce(onlyAlertOnce);
            } else {
                mBuilder25.setOnlyAlertOnce(onlyAlertOnce);
            }
            return this;
        }

        Builder setProgress(int max, int progress, boolean indeterminate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setProgress(max, progress, indeterminate);
            } else {
                mBuilder25.setProgress(max, progress, indeterminate);
            }
            return this;
        }

        Builder setWhen(long w) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setWhen(w);
            } else {
                mBuilder25.setWhen(w);
            }
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setAutoCancel(autoCancel);
            } else {
                mBuilder25.setAutoCancel(autoCancel);
            }

            return this;
        }

        /**
         * 大于等于Android 8.0 api>=26
         *
         * @return
         */
        @TargetApi(Build.VERSION_CODES.O)
        private Notification.Builder getChannelNotification(Context context, String channelId) {
            return new Notification.Builder(context, channelId);
        }

        /**
         * 小于Android 8.0 api<26
         *
         * @return
         */
        private NotificationCompat.Builder getNotification_25(Context context) {
            return new NotificationCompat.Builder(context);
        }

        public NotificationApiCompat builder() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(mNotificationChannel);
                mNotification = mBuilder26.build();
            } else {
                mNotification = mBuilder25.build();
            }
            return new NotificationApiCompat(this);
        }
    }

}
