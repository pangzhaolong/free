package com.donews.keepalive.accountsync;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.concurrent.TimeUnit;

public class AccountJobService extends AccountCoreJobService{
    private static final long duration = TimeUnit.SECONDS.toMillis(5L);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        super.onStartJob(params);
        if (Build.VERSION.SDK_INT >= 24) {
            startJob(getApplicationContext());
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        super.onStopJob(params);
        return false;
    }

    public static void startJob(Context context) {
        //最短延迟时间 setMinimumLatency
        //最长延迟时间 setOverrideDeadline
        //重复每周期间隔时间 setPeriodic
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            JobInfo.Builder persisted = (new JobInfo.Builder(1000, new ComponentName(context, AccountJobService.class))).setPersisted(true);
            if (Build.VERSION.SDK_INT < 24) {
                persisted.setPeriodic(duration);
            } else {
                persisted.setMinimumLatency(duration);
            }
            persisted.setBackoffCriteria(TimeUnit.MINUTES.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR);  //线性重试方案
            try {
                jobScheduler.schedule(persisted.build());
            } catch (Throwable ignored) {
            }
        }
    }

}
