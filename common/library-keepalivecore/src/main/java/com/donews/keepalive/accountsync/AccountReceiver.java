package com.donews.keepalive.accountsync;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

public class AccountReceiver {

    public static void register(Context context, BroadcastReceiver r) {
        IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.QUICKBOOT_POWERON");
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("android.intent.action.QUICKBOOT_POWEROFF");
        unregister(context, r);
        context.registerReceiver(r, filter);
        notKillReceiver(context, r.getClass());
    }

    public static void unregister(Context context, BroadcastReceiver r) {

        try {
            context.unregisterReceiver(r);
        } catch (Throwable ignored) {
        }

        killReceiver(context, r.getClass());
    }

    private static void killReceiver(Context context, Class<?> clazz) {
        try {
            ComponentName componentName;
            if (context instanceof Application) {
                componentName = new ComponentName(context, clazz.getName());
            } else {
                Context app = context.getApplicationContext();
                componentName = new ComponentName(app.getPackageName(), clazz.getName());
            }

            ComponentName runningReceiver = componentName;
            context.getPackageManager().setComponentEnabledSetting(runningReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Throwable ignored) {
        }
    }

    private static void notKillReceiver(Context context, Class<?> clazz) {
        try {
            ComponentName componentName;
            if (context instanceof Application) {
                componentName = new ComponentName(context, clazz.getName());
            } else {
                Context app = context.getApplicationContext();
                componentName = new ComponentName(app.getPackageName(), clazz.getName());
            }

            ComponentName newReceiver = componentName;
            if (context.getPackageManager().getComponentEnabledSetting(newReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                context.getPackageManager().setComponentEnabledSetting(newReceiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
        } catch (Throwable ignored) {
        }
    }

}
