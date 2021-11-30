package com.donews.keepalive;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.keepalive.daemon.core.R;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static com.donews.keepalive.JobHandlerService.JOB_ID_3;


public class DazzleActivity extends Activity {

    public static WeakReference<DazzleActivity> sKeepLiveActivity = null;


    private static final LaunchStart launchStart = new LaunchStart();
    private static int count = 0;


    static void destroyOnePixelActivity() {
        Activity activity = sKeepLiveActivity == null ? null : sKeepLiveActivity.get();
        if (activity != null) activity.finish();

    }

    static void showOnePixelActivity(Context context) {
        Activity activity = sKeepLiveActivity == null ? null : sKeepLiveActivity.get();
        if (activity != null && !activity.isDestroyed()) {
            activity.finish();
        }

        Intent intent = new Intent(context,DazzleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchStart.doStart(context, intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationUtils.clearAllNotification(this);
        launchStart.cancel();
        sKeepLiveActivity = new WeakReference<>(this);
        initWindow();

        if (count == 2) {
            count += 2;
        } else if (count < 4) {
            count += 1;
        } else {
            count = 4;
        }

        if (!JobHandlerService.isServiceRunning(getApplicationContext(),DazzleService.class.getName())) {
            Intent localIntent = new Intent(this, DazzleService.class);
            localIntent.putExtra("type", "2");
            try {
                startService(localIntent);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (Objects.equals(Intent.ACTION_SCREEN_ON, ScreenStateReceiver.action) || Objects.equals(Intent.ACTION_USER_PRESENT,ScreenStateReceiver.action)) {
            finish();
            return;
        }

        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID_3,new ComponentName(this, DazzleLiveService.class))
                .setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS,JobInfo.BACKOFF_POLICY_LINEAR )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(true) // 当插入充电器，执行该任务
                .setMinimumLatency((DazzleReal.debug ? 15 * 1000L : 60 * 1000L * 15) * count)
                .setOverrideDeadline((DazzleReal.debug ? 20 * 1000L : 60 * 1000L * 20) * count);
        mJobScheduler.schedule(builder.build());
    }


    private void initWindow() {
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.height = 1;
        layoutParams.width = 1;

        if (DazzleReal.debug) {
            layoutParams.height = 100;
            layoutParams.width = 100;
            window.setBackgroundDrawableResource(R.drawable.one_pixel_bg);
        }

        window.setAttributes(layoutParams);
    }

}
