package com.keepalive.daemon.core.component;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import com.donews.keepalive.global.KeepAliveGlobalConfig;
import com.keepalive.daemon.core.utils.ServiceHolder;

public class DaemonInstrumentation extends Instrumentation {
    public static final String TAG = KeepAliveGlobalConfig.TAG;
    @Override
    public void callApplicationOnCreate(Application application) {
        super.callApplicationOnCreate(application);
        Log.i(TAG,"callApplicationOnCreate onCreate,pid="+ Process.myPid());
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG,"DaemonInstrumentation onCreate,pid="+ Process.myPid());
        ServiceHolder.fireService(getTargetContext(), DaemonService.class, false);
    }
}
