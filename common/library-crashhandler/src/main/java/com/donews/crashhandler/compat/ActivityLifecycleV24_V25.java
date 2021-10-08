package com.donews.crashhandler.compat;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import com.donews.crashhandler.core.IActivityLifecycleCrashHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 适配SDK 24-25
 *
 * @author Swei
 * @date 2021/4/8 20:31
 * @since v1.0
 */
public class ActivityLifecycleV24_V25 implements IActivityLifecycleCrashHandler {

    @Override
    public void finishLaunchActivity(Message message) {
        try {
            Object activityClientRecord = message.obj;

            Field tokenField = activityClientRecord.getClass().getDeclaredField("token");

            tokenField.setAccessible(true);
            IBinder binder = (IBinder) tokenField.get(activityClientRecord);
            finish(binder);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishResumeActivity(Message message) {
        try {
            finish((IBinder) message.obj);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishPauseActivity(Message message) {
        try {
            finish((IBinder) message.obj);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishStopActivity(Message message) {
        try {
            finish((IBinder) message.obj);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void finish(IBinder binder) throws Exception {

        //ActivityManagerNative.getDefault().finishActivity(r.token, Activity.RESULT_CANCELED, null, Activity.DONT_FINISH_TASK_WITH_ACTIVITY);
        Class activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

        Method getDefaultMethod = activityManagerNativeClass.getDeclaredMethod("getDefault");

        Object activityManager = getDefaultMethod.invoke(null);


        Method finishActivityMethod = activityManager.getClass().getDeclaredMethod("finishActivity", IBinder.class, int.class, Intent.class, int.class);
        int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
        finishActivityMethod.invoke(activityManager, binder, Activity.RESULT_CANCELED, null, DONT_FINISH_TASK_WITH_ACTIVITY);

    }
}
