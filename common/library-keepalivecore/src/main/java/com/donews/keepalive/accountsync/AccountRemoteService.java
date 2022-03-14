package com.donews.keepalive.accountsync;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.donews.keepalive.ISyncAccountInterface;

public class AccountRemoteService extends Service {

    private RemoteBinder binder;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public static final class RemoteBinder extends ISyncAccountInterface.Stub {
    }

    public final class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AccountRemoteService.this.startService(new Intent(AccountRemoteService.this, AccountLocalService.class));
            AccountRemoteService.this.bindService(new Intent(AccountRemoteService.this, AccountLocalService.class),
                    AccountRemoteService.this.new MyServiceConnection(), Context.BIND_AUTO_CREATE);
        }
    }

    public static final class InnerService extends Service {
        @Override
        public void onCreate() {
            super.onCreate();
            this.startForeground(10, new Notification());
            this.stopSelf();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
