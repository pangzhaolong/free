package com.donews.keepalive.accountsync;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.donews.keepalive.ISyncAccountInterface;

public class AccountLocalService extends Service {

    private AccountLocalService.LocalBinder myBinder;

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.myBinder = new AccountLocalService.LocalBinder();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("deamon", "deamon", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);
            Notification notification =
                    (new Notification.Builder(this, "deamon")).setAutoCancel(true).setCategory("service")
                            .setOngoing(true)
                            .setPriority(Notification.PRIORITY_MAX).build();
            this.startForeground(10, notification);
        } else if (Build.VERSION.SDK_INT >= 18) {
            this.startForeground(10, new Notification());
            this.startService(new Intent(this, AccountLocalService.InnerService.class));
        } else {
            this.startForeground(10, new Notification());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //ISyncAccountInterface.aidl
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, AccountRemoteService.class),
                new MyServiceConnection(), Context.BIND_AUTO_CREATE);
        return super.onStartCommand(intent, flags, startId);
    }

    public static final class LocalBinder extends ISyncAccountInterface.Stub {
    }

    public static final class InnerService extends Service {
        public void onCreate() {
            super.onCreate();
            this.startForeground(10, new Notification());
            this.stopSelf();
        }
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public final class MyServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        public void onServiceDisconnected(ComponentName componentName) {
            AccountLocalService.this.startService(new Intent(AccountLocalService.this, AccountRemoteService.class));
            AccountLocalService.this.bindService(new Intent(AccountLocalService.this, AccountRemoteService.class),
                    AccountLocalService.this.new MyServiceConnection(), Context.BIND_AUTO_CREATE);
        }
    }

}
