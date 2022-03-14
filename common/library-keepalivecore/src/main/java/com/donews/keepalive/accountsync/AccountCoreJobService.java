package com.donews.keepalive.accountsync;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * 为何要jobservice 看这篇文章https://www.jianshu.com/p/aba38b9e11e7
 */
public class AccountCoreJobService extends JobService {
    public static final String TAG = "keepAlive-Account";

    @Override
    public boolean onStartJob(JobParameters params) {
        String service = AccountRemoteService.class.getName();
        boolean remoteServiceIsRunning = isServiceRunning(this, service);
        service = AccountLocalService.class.getName();
        boolean localServiceIsRunning = isServiceRunning(this, service);
        if (remoteServiceIsRunning && localServiceIsRunning) {
            Log.i(TAG, "remote and local service is running");
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private boolean isServiceRunning(Context context, String serviceName) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(10);
            Iterator<ActivityManager.RunningServiceInfo> iterator = runningServices.iterator();

            ComponentName componentName;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                ActivityManager.RunningServiceInfo runningService = iterator.next();
                componentName = runningService.service;
            } while (!TextUtils.equals(componentName.getClassName(), serviceName));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return true;
    }

}
