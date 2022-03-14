package com.donews.keepalive;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.NonNull;

import static com.donews.keepalive.JobHandlerService.JOB_ID;


public class JobHeartService extends JobIntentService {

    public static long LAST_TIME = 0L;//上一次记录的时间戳

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, JobHeartService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String type = intent.getStringExtra("type") == null ? "0" : intent.getStringExtra("type");
        long time = intent.getLongExtra("time", 0L);//首次启动服务时间
        if (LAST_TIME == 0L) {
            LAST_TIME = time;
        }
        long currTime = SystemClock.elapsedRealtime();
        DazzleReal.callback.doReport(type, android.os.Process.myPid(), currTime - time, currTime - LAST_TIME);
        LAST_TIME = currTime;
    }
}
