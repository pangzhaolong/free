package debug;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.keepalive.daemon.core.R;


class NotificationApiCompat {
    private final NotificationManager manager;
    Notification notificationApiCompat;
    private final Notification.Builder mBuilder26;
    private final NotificationCompat.Builder mBuilder25;

    static RemoteViews remoteViews = null;
    static RemoteViews remoteViews2 = null;
    static RemoteViews remoteViews3 = null;

    private Builder builder;

    public NotificationApiCompat(Builder builder) {
        manager = builder.manager;
        notificationApiCompat = builder.mNotification;
        mBuilder26 = builder.mBuilder26;
        mBuilder25 = builder.mBuilder25;
    }


    void notify(int id) {
        manager.notify(id, notificationApiCompat);
    }

    void updateNotification(int id, Bitmap img) {
        builder.initRemoteViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (img != null) {
                if (remoteViews != null) {
                    remoteViews.setImageViewBitmap(R.id.iv_img, img);
                }
                if (remoteViews2 != null) {
                    remoteViews2.setImageViewBitmap(R.id.iv_img, img);
                }
            }
            notificationApiCompat = mBuilder26.build();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (img != null) {
                if (remoteViews != null) {
                    if (!ROMUtil12.isVivo()) {
                        remoteViews.setImageViewBitmap(R.id.iv_img, img);
                    }
                }
                if (remoteViews2 != null) {
                    remoteViews2.setImageViewBitmap(R.id.iv_img, img);
                }
            }
            notificationApiCompat = mBuilder25.build();
        } else {
            if (img != null) {
                if (remoteViews3 != null) {
                    remoteViews3.setImageViewBitmap(R.id.iv_img, img);
                }
            }
            notificationApiCompat = mBuilder25.build();
        }
        manager.notify(id, notificationApiCompat);
    }


    static class Builder {
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

        Builder setPriority(int pri) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setPriority(pri);
            } else {
                mBuilder25.setPriority(pri);
            }
            return this;
        }

        Builder setLargeIcon(Bitmap icon) {
            if (ROMUtil12.isMiui()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mBuilder26.setLargeIcon(icon);
                } else {
                    mBuilder25.setLargeIcon(icon);
                }
            }
            return this;
        }

        Builder setContentIntent(PendingIntent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setContentIntent(intent);
            } else {
                mBuilder25.setContentIntent(intent);
            }
            return this;
        }

        Builder setTicker(CharSequence tickerText) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setTicker(tickerText);
            } else {
                mBuilder25.setTicker(tickerText);
            }
            return this;
        }

        Builder setColor() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setColor(mContext.getResources().getColor(R.color.teal_200));
            } else {
                mBuilder25.setColor(mContext.getResources().getColor(R.color.teal_200));
            }
            return this;
        }

        Builder setContent() {
            initRemoteViews();
            return this;
        }

        void initRemoteViews() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
                remoteViews2 = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
                mBuilder26.setCustomBigContentView(remoteViews);
                mBuilder26.setCustomContentView(remoteViews2);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!ROMUtil12.isVivo()) {
                    remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
                    mBuilder25.setCustomBigContentView(remoteViews);
                }
                remoteViews2 = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
                mBuilder25.setCustomContentView(remoteViews2);
            } else {
                remoteViews3 = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
                mBuilder25.setContent(remoteViews3);
            }
        }

        Builder setStyle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setStyle(new Notification.MediaStyle());
            } else {
                mBuilder25.setStyle(new NotificationCompat.BigPictureStyle());
            }
            return this;
        }

        Builder setCategory() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setCategory(Notification.CATEGORY_SERVICE);
            } else {
                mBuilder25.setCategory(Notification.CATEGORY_SERVICE);
            }
            return this;
        }

        Builder setContentTitle(CharSequence title) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setContentTitle(title);
            } else {
                mBuilder25.setContentTitle(title);
            }
            return this;
        }

        Builder setContentText(CharSequence text) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setContentText(text);
            } else {
                mBuilder25.setContentText(text);
            }
            return this;
        }

        Builder setOngoing(Boolean ongoing) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder26.setOngoing(ongoing);
            } else {
                mBuilder25.setOngoing(ongoing);
            }
            return this;
        }

        Builder setOnlyAlertOnce(Boolean onlyAlertOnce) {
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

        Builder setAutoCancel(boolean autoCancel) {
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

        NotificationApiCompat builder() {
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
