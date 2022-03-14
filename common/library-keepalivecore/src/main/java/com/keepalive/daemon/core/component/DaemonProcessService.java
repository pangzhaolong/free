package com.keepalive.daemon.core.component;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.donews.keepalive.global.KeepAliveGlobalConfig;

public class DaemonProcessService extends Service {
    public static final String TAG = KeepAliveGlobalConfig.TAG;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"DaemonProcessService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"DaemonProcessService onDestroy , pid = "+ Process.myPid());
    }
}
