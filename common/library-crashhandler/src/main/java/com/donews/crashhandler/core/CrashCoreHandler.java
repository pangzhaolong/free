package com.donews.crashhandler.core;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.donews.crashhandler.compat.ActivityLifecycleV15_V20;
import com.donews.crashhandler.compat.ActivityLifecycleV21_V23;
import com.donews.crashhandler.compat.ActivityLifecycleV24_V25;
import com.donews.crashhandler.compat.ActivityLifecycleV26_V27;
import com.donews.crashhandler.compat.ActivityLifecycleV28_V30;
import com.donews.crashhandler.reflect.ReflectionLimit;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;

/**
 * 入口类，但是面临的问题是jni的crash暂时无能为力
 *
 * @author Swei
 * @date 2021/4/8 15:20
 * @since v1.0
 */
public class CrashCoreHandler {
    // https://www.androidos.net.cn/android/10.0.0_r6/tree  的frameworks/base/core/java/android/app/ActivityThread
    private static final String TAG = "CrashCore";

    private static final int LAUNCH_ACTIVITY = 100;
    private static final int PAUSE_ACTIVITY = 101;
    private static final int PAUSE_ACTIVITY_FINISHING = 102;
    private static final int STOP_ACTIVITY_HIDE = 104;
    private static final int RESUME_ACTIVITY = 107;
    private static final int DESTROY_ACTIVITY = 109;
    private static final int EXECUTE_TRANSACTION = 159;

    //Handler.post方式可以保证不影响该条消息中后面的逻辑
    private static final android.os.Handler sHandler = new android.os.Handler(Looper.getMainLooper());

    private static Thread.UncaughtExceptionHandler sUncaughtExceptionHandler;

    private static boolean sInstalled = false;
    private static boolean sJavaCrashRunning = false;
    private static IActivityLifecycleCrashHandler sActivityLifecycleCrashHandler;
    private static LifecycleHandlerCallback sLifecycleHandlerCallback;

    private static Runnable javaCodeCrashHandlerTask;


    private CrashCoreHandler() {
    }

    public static void install(){
        install(true);
    }

    public static void install(boolean lifecycleHandler){
        if(sInstalled){
            return;
        }
        sInstalled = true;

        //处理生命周期时异常，解决黑屏问题，处理办法finish，同时激活主线程crash抓捕
        if(lifecycleHandler && ReflectionLimit.clearLimit()){
            initActivityLifecycleCrashHandler();
        }
        //处理代码调用时异常
        initJavaCodeCrashHandler();
    }

    private static Runnable getJavaCodeCrashHandlerTask(){
        if(javaCodeCrashHandlerTask == null){
            javaCodeCrashHandlerTask = () -> {
                while (sJavaCrashRunning) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        notifyMainException(e);
                        isChoreographerException(e);
                        if (e instanceof CrashHandlerQuitException) {
                            sJavaCrashRunning = false;
                            return;
                        }
                    }
                }
            };
        }
        return javaCodeCrashHandlerTask;
    }

    public static boolean installed(){
        return sInstalled;
    }

    public static void uninstall() {
        if (!sInstalled) {
            return;
        }
        resetActivityLifecycleCrashHandler();
        resetJaveCodeCrashHandler();
        sInstalled = false;
    }

    private static void initJavaCodeCrashHandler() {
        //如果已经设置了unCaughtException保存
        sUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (t == Looper.getMainLooper().getThread()) {
                    isChoreographerException(e);
                    mainThreadCrashHandler();
                }else{
                    notifyThreadException(e);
                }
            }
        });
        mainThreadCrashHandler();
    }

    private static void mainThreadCrashHandler() {
        if (sJavaCrashRunning) {
            return;
        }
        sJavaCrashRunning = true;
        sHandler.post(getJavaCodeCrashHandlerTask());
    }

    //处理view渲染导致的通知问题
    private static void isChoreographerException(Throwable e) {
        if (e == null) {
            return;
        }
        StackTraceElement[] elements = e.getStackTrace();
        if (elements == null) {
            return;
        }

        for(int i = elements.length - 1; i > -1; i--) {
            if (elements.length - i > 20) {
                return;
            }
            StackTraceElement element = elements[i];
            if ("android.view.Choreographer".equals(element.getClassName())
                    && "Choreographer.java".equals(element.getFileName())
                    && "doFrame".equals(element.getMethodName())) {
                notifyUIDrawException(e);
                return;
            }
        }
    }

    private static void resetJaveCodeCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(sUncaughtExceptionHandler);
        try{
            sHandler.removeCallbacks(getJavaCodeCrashHandlerTask());
        }catch (Throwable t){
        	t.printStackTrace();
        }
        sJavaCrashRunning = false;
    }

    private static void resetActivityLifecycleCrashHandler() {
        try {
            android.os.Handler mHHandler = null;
            if (sLifecycleHandlerCallback != null && sLifecycleHandlerCallback.getHandler() != null) {
                mHHandler = sLifecycleHandlerCallback.getHandler();
            } else {
                Class activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null);
                Field mhField = activityThreadClass.getDeclaredField("mH");
                mhField.setAccessible(true);
                mHHandler = (android.os.Handler) mhField.get(activityThread);
            }
            if (mHHandler != null) {
                Field callbackField = android.os.Handler.class.getDeclaredField("mCallback");
                callbackField.setAccessible(true);
                callbackField.set(mHHandler, null);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void initActivityLifecycleCrashHandler() {
        if(sActivityLifecycleCrashHandler == null){
            if (Build.VERSION.SDK_INT >= 28) {
                sActivityLifecycleCrashHandler = new ActivityLifecycleV28_V30();
            } else if (Build.VERSION.SDK_INT >= 26) {
                sActivityLifecycleCrashHandler = new ActivityLifecycleV26_V27();
            } else if (Build.VERSION.SDK_INT == 25 || Build.VERSION.SDK_INT == 24) {
                sActivityLifecycleCrashHandler = new ActivityLifecycleV24_V25();
            } else if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 23) {
                sActivityLifecycleCrashHandler = new ActivityLifecycleV21_V23();
            }else {
                sActivityLifecycleCrashHandler = new ActivityLifecycleV15_V20();
            }
        }

        try{
            //此时注入当前app下核心handler，这个handler是用来发送通知，因此当接收到生命周期通知时，根据异常情况try catch住
            //mCallback 返回是false则不影响之前逻辑，为true则中断
            //android.os.Handler handler = new android.os.Handler();
            //   public void dispatchMessage(@NonNull Message msg) {
            //        if (msg.callback != null) {
            //            handleCallback(msg);
            //        } else {
            //            if (mCallback != null) {
            //                if (mCallback.handleMessage(msg)) {
            //                    return;
            //                }
            //            }
            //            handleMessage(msg);
            //        }
            //    }
            if(sLifecycleHandlerCallback == null){
                Class activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null);

                Field mhField = activityThreadClass.getDeclaredField("mH");
                mhField.setAccessible(true);
                final android.os.Handler mHHandler = (android.os.Handler) mhField.get(activityThread);
                if(mHHandler != null){
                    Field callbackField = android.os.Handler.class.getDeclaredField("mCallback");
                    callbackField.setAccessible(true);
                    sLifecycleHandlerCallback = new LifecycleHandlerCallback(mHHandler);
                    callbackField.set(mHHandler,sLifecycleHandlerCallback);
                }
            }else{
                Field callbackField = android.os.Handler.class.getDeclaredField("mCallback");
                callbackField.setAccessible(true);
                callbackField.set(sLifecycleHandlerCallback.getHandler(),sLifecycleHandlerCallback);
            }
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    /////////////////////////////////////此处实现额外日志/////////////////////////////////////
    private static void notifyLifecycleException(Throwable throwable) {
        Log.e(TAG,"notifyLifecycleException",throwable);
        reportBugly(throwable);
    }

    private static void notifyMainException(Throwable throwable){
        if(!(throwable instanceof CrashHandlerQuitException)){
            Log.e(TAG,"notifyMainException",throwable);
            reportBugly(throwable);
        }
    }

    private static void notifyThreadException(Throwable throwable){
        Log.e(TAG,"notifyThreadException",throwable);
        reportBugly(throwable);
    }

    private static void notifyUIDrawException(Throwable throwable){
        Log.e(TAG,"notifyUIDrawException",throwable);
    }

    private static class LifecycleHandlerCallback implements android.os.Handler.Callback{
        private android.os.Handler mHandler;

        public LifecycleHandlerCallback(Handler mHandler) {
            this.mHandler = mHandler;
        }

        public Handler getHandler() {
            return mHandler;
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.w(TAG,"msg what = "+msg.what+"");
            if (Build.VERSION.SDK_INT >= 28) {//android P 以上
                if (msg.what == EXECUTE_TRANSACTION) {
                    try {
                        mHandler.handleMessage(msg);
                    } catch (Throwable throwable) {//出现异常时
                        sActivityLifecycleCrashHandler.finishLaunchActivity(msg);
                        notifyLifecycleException(throwable);
                    }
                    return true;
                }
                return false;
            }
            switch (msg.what) {
                case LAUNCH_ACTIVITY://开启新页面时
                    try {
                        mHandler.handleMessage(msg);
                    } catch (Throwable throwable) {
                        sActivityLifecycleCrashHandler.finishLaunchActivity(msg);
                        notifyLifecycleException(throwable);
                    }
                    return true;
                case RESUME_ACTIVITY://回到activity onRestart onStart onResume
                    try {
                        mHandler.handleMessage(msg);
                    } catch (Throwable throwable) {
                        sActivityLifecycleCrashHandler.finishResumeActivity(msg);
                        notifyLifecycleException(throwable);
                    }
                    return true;
                case PAUSE_ACTIVITY_FINISHING://按返回键，onPause
                case PAUSE_ACTIVITY://开启新页面时，旧页面执行 activity.onPause
                    try {
                        mHandler.handleMessage(msg);
                    } catch (Throwable throwable) {
                        sActivityLifecycleCrashHandler.finishPauseActivity(msg);
                        notifyLifecycleException(throwable);
                    }
                    return true;
                case STOP_ACTIVITY_HIDE://开启新页面时，旧页面执行 activity.onStop
                    try {
                        mHandler.handleMessage(msg);
                    } catch (Throwable throwable) {
                        sActivityLifecycleCrashHandler.finishStopActivity(msg);
                        notifyLifecycleException(throwable);
                    }
                    return true;
                case DESTROY_ACTIVITY:// 关闭activity，onStop  onDestroy
                    try {
                        mHandler.handleMessage(msg);
                    } catch (Throwable throwable) {
                        notifyLifecycleException(throwable);
                    }
                    return true;
            }
            return false;
        }
    }

    private static void reportBugly(Throwable throwable){
        try{
            CrashReport.postCatchedException(throwable);
        }catch (Throwable t){
        	t.printStackTrace();
        }
    }

}
