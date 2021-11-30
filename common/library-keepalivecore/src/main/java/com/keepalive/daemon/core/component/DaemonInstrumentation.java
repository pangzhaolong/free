package com.keepalive.daemon.core.component;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Bundle;

import com.keepalive.daemon.core.utils.Logger;
import com.keepalive.daemon.core.utils.ServiceHolder;

public class DaemonInstrumentation extends Instrumentation {
    @Override
    public void callApplicationOnCreate(Application application) {
        super.callApplicationOnCreate(application);
        Logger.v(Logger.TAG, "DaemonInstrumentation callApplicationOnCreate");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Logger.v(Logger.TAG, "DaemonInstrumentation onCreate");
        ServiceHolder.fireService(getTargetContext(), DaemonService.class, false);
    }
}
