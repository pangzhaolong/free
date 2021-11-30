package com.donews.alive.launch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.keepalive.daemon.core.utils.Logger;


public class AlarmHelper {

    private static AlarmHelper alarmHelper;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private Context context;
    private Intent intent;
    public static final String TAG = "LaunchStart";

    public synchronized static AlarmHelper getInstance(Context context) {
        if (alarmHelper == null) {
            alarmHelper = new AlarmHelper(context);
        }
        return alarmHelper;
    }

    private AlarmHelper(Context context) {
        this.context = context;
    }

    public AlarmHelper setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }


    /**
     * 执行闹钟
     *
     * @param delayTime 闹钟等待时间
     */
    public void scheduleNextJob(long delayTime) {
        Logger.d(TAG, "闹钟1开始定时");

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null) {
            Logger.d(TAG, "如果不为空，说明有任务在，直接返回");
            return;
        }
        if (intent == null) {
            return;
        }
        int sceneId = 0;
        try {
            sceneId = (Integer) intent.getExtras().get("sceneId");
        } catch (Throwable t) {
        }
        //根据场景ID来区分任务
        pendingIntent = PendingIntent.getActivity(context, sceneId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            /** 即使系统处于低功耗（doze）模式下也能执行唤醒操作，应用程序会被添加到临时系统白名单10秒钟
             *在正常系统运行情况下，他将不会超过每分钟发送一次这些alarm，在低功耗空闲模式下，这个持续时间可能要长的多，比如15分钟；
             * 不管应用程序时那个版本，这个调用都会被允许处理
             *
             * 再加入系统白名单的10秒钟，做启动activity操作
             * */
            alarmMgr.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, delayTime, pendingIntent);

            //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, dalyTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, delayTime, pendingIntent);
        } else {
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delayTime, pendingIntent);
        }
        Logger.d(TAG, "执行定时任务完毕");
    }

    /**
     * 主动清除
     */
    public void cancel() {
        if (alarmMgr != null && pendingIntent != null) {
            alarmMgr.cancel(pendingIntent);
            alarmHelper = null;
            Logger.d(TAG, "还原闹钟");
        }
    }
}
