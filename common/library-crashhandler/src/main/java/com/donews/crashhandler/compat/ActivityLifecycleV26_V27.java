package com.donews.crashhandler.compat;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import com.donews.crashhandler.core.IActivityLifecycleCrashHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 适配SDK26
 *
 * @author Swei
 * @date 2021/4/8 20:31
 * @since v1.0
 */
public class ActivityLifecycleV26_V27 implements IActivityLifecycleCrashHandler {

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
            finishSomeArgs(message);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishPauseActivity(Message message) {
        try {
            finishSomeArgs(message);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishStopActivity(Message message) {
        try {
            finishSomeArgs(message);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void finishSomeArgs(Message message) {
        try {
            Object someArgs = message.obj;
            Field arg1Field = someArgs.getClass().getDeclaredField("arg1");
            arg1Field.setAccessible(true);
            IBinder binder = (IBinder) arg1Field.get(someArgs);
            finish(binder);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void finish(IBinder binder) throws Exception {
        //handleDestroyActivity((IBinder)msg.obj, msg.arg1 != 0,msg.arg2, false);
        //ActivityManager.getService().finishActivity(mToken, resultCode, resultData, finishTask)
        Method getServiceMethod = ActivityManager.class.getDeclaredMethod("getService");
        Object activityManager = getServiceMethod.invoke(null);

        Method finishActivityMethod = activityManager.getClass().getDeclaredMethod("finishActivity", IBinder.class, int.class, Intent.class, int.class);
        finishActivityMethod.setAccessible(true);
        int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
        finishActivityMethod.invoke(activityManager, binder, Activity.RESULT_CANCELED, null, DONT_FINISH_TASK_WITH_ACTIVITY);
    }

}
