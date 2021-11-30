package com.donews.keepalive;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.util.List;

@SuppressLint("NewApi")
public class JobHandlerService extends JobService {
    public static final int JOB_ID = 100001;
    public static final int JOB_ID_2 = 100002;
    public static final int JOB_ID_3 = 100003;

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (TextUtils.equals(className, si.service.getClassName())) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startFg();//启动通知
        startService(this);//启动本地服务
        startJobScheduler();//启动任务
        return Service.START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        startFg();
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= 21) {
            JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            mJobScheduler.cancel(JOB_ID_2);
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startFg();//启动通知
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (!isServiceRunning(
                getApplicationContext(),
                DazzleService.class.getName()
        )
        ) {
            startService(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (!isServiceRunning(
                getApplicationContext(),
                DazzleService.class.getName()
        )
        ) {
            startService(this);
        }
        return false;
    }

    private void startJobScheduler() {
        if (Build.VERSION.SDK_INT >= 21) {
            JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder =
                    new JobInfo.Builder(
                            JOB_ID_2,
                            new ComponentName(getPackageName(), JobHandlerService.class.getName())
                    );
            if (Build.VERSION.SDK_INT >= 24) {
                builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);//执行的最小延迟时间
                builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最长延时时间
                builder.setBackoffCriteria(
                        JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS,
                        JobInfo.BACKOFF_POLICY_LINEAR
                );//线性重试方案
            } else {
                builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            }
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresCharging(true); // 当插入充电器，执行该任务
            mJobScheduler.schedule(builder.build());
        }
    }

    private void startFg() {
        CommonNotification.startForeground(this);
    }

    private void startService(Context context) {
        //启动本地服务
        Intent localIntent = new Intent(context, DazzleService.class);
        startService(localIntent);
        DazzleReal.serviceStart = true;
        DazzleReal.regReceiver(context);
    }
}
