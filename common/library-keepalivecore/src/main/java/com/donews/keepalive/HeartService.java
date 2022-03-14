package com.donews.keepalive;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import static com.donews.keepalive.JobHeartService.LAST_TIME;


public class HeartService extends IntentService {

    public HeartService() {
        super(HeartService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String type = intent.getStringExtra("type") == null ? "0" : intent.getStringExtra("type");
        long time = intent.getLongExtra("time", 0L);//首次启动服务时间
        if (LAST_TIME == 0L) {
            LAST_TIME = time;
        }
        long currTime = SystemClock.elapsedRealtime();
        DazzleReal.callback.doReport(
                type,
                android.os.Process.myPid(),
                currTime - time,
                currTime - LAST_TIME
        );
        LAST_TIME = currTime;
    }
}
