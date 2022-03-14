package com.donews.keepalive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

public class AlarmHelper {

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;

    //下一次任务执行时间
    public void scheduleNextJob(Context context, Intent intent, long dalyTime) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null) {
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
        pendingIntent = PendingIntent.getActivity(context, sceneId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
            if (Build.VERSION.SDK_INT >= 29) {
                intent.putExtra("channelId", 6);
                context.startActivity(intent);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, dalyTime, pendingIntent);
        } else {
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, dalyTime, pendingIntent);
        }
    }

    /**
     * 主动清除
     */
    public void cancel() {
        try {
            if (alarmMgr != null && pendingIntent != null) {
                alarmMgr.cancel(pendingIntent);
            }
            pendingIntent = null;
        } catch (Throwable t) {
        }
    }
}
