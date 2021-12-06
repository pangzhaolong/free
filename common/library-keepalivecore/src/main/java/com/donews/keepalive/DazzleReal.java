package com.donews.keepalive;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;

public class DazzleReal {

    static boolean init;
    static boolean debug;
    static RealCallback callback;
    static Notification notification;
    static int id;
    static ForegroundNotificationClickListener foregroundNotificationClickListener;

    static Application application;
    static boolean serviceStart = false;


    static String[] STATE_INTENTS = new String[]{
            "android.intent.action.PHONE_STATE",
            "android.intent.action.PHONE_STATE_2",
            "android.intent.action.PHONE_STATE2",
            "android.intent.action.DUAL_PHONE_STATE",
            "android.intent.action.NEW_OUTGOING_CALL",
            Intent.ACTION_SCREEN_OFF,
            Intent.ACTION_SCREEN_ON,
            "android.intent.action.USER_PRESENT",
            "android.intent.action.BOOT_COMPLETED",
            "android.net.conn.CONNECTIVITY_CHANGE",
            "android.intent.action.MEDIA_MOUNTED",
            "android.intent.action.ACTION_POWER_CONNECTED",
            "android.intent.action.ACTION_POWER_DISCONNECTED",
            "android.intent.action.PACKAGE_ADDED",
            "android.net.wifi.WIFI_STATE_CHANGED",
            "android.net.wifi.STATE_CHANGED",
            "android.intent.action.MEDIA_EJECT",
            "com.cootek.smartdialer.action.PHONE_STATE",
            "com.cootek.smartdialer.action.INCOMING_CALL",
            "android.provider.Telephony.SMS_RECEIVED",
            "com.tencent.mm.plugin.openapi.Intent.ACTION_HANDLE_APP_UNREGISTER",
            "com.tencent.mm.plugin.openapi.Intent.ACTION_HANDLE_APP_REGISTER"
    };


    private static final ScreenStateReceiver sScreenStateReceiver = new ScreenStateReceiver();
    private static final SystemNotifyReceiver mSystemNotifyReceiver = new SystemNotifyReceiver();

    public static void init(Application context, Boolean debug, Notification notification, int id, RealCallback callback,ForegroundNotificationClickListener listener) {

        if (init || !isMainProcess(context)) {
            return;
        }

        init = true;
        DazzleReal.debug = debug;

        DazzleReal.callback = callback;
        if (notification != null) {
            DazzleReal.notification = notification;
        }
        DazzleReal.foregroundNotificationClickListener = listener;
        if (id != 0) {
            DazzleReal.id = id;
        }

        DazzleReal.application = context;
        startKeep(application);

    }

    private static void startKeep(Application application) {
        //启动服务
        startService(application);
    }

    private static void startService(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            //21及以上版本
            Intent intent = new Intent(context, JobHandlerService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                try {
                    context.startForegroundService(intent);
                } catch (Throwable t) {
                }
            } else {
                context.startService(intent);
            }
        } else {
            //低于21版本
            //启动本地服务
            Intent localIntent = new Intent(context, DazzleService.class);
            context.startService(localIntent);
            serviceStart = true;
            regReceiver(context);
        }
    }

    public static void regReceiver(Context context) {

        try {
            IntentFilter intentFilter = new IntentFilter();
            // 亮屏时触发
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            // 锁屏时触发
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            // 解锁成功后触发
            intentFilter.addAction(Intent.ACTION_USER_PRESENT);
            context.registerReceiver(sScreenStateReceiver, intentFilter);
        } catch (Throwable t) {
        }

        try {
            IntentFilter filter = new IntentFilter();
            for (String stateIntent : STATE_INTENTS) {
                filter.addAction(stateIntent);
            }
            context.registerReceiver(mSystemNotifyReceiver, filter);
        } catch (Throwable t) {
        }
    }

    public static void unregReceiver(Context context) {
        try{
            context.unregisterReceiver(sScreenStateReceiver);
        }catch (Throwable t){
        	t.printStackTrace();
        }
        try{
            context.unregisterReceiver(mSystemNotifyReceiver);
        }catch (Throwable t){
        	t.printStackTrace();
        }
    }

    private static boolean isMainProcess(Application app) {
        int pid = android.os.Process.myPid();
        String processName = "";
        try {
            ActivityManager mActivityManager = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName;
                    break;
                }
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return TextUtils.equals(processName, app.getPackageName());
    }
}
