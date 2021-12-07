package com.donews.notify.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.donews.common.NotifyLuncherConfigManager;
import com.donews.keepalive.LaunchStart;

import java.lang.ref.WeakReference;

public class NotifyActionActivity extends Activity {

    private static WeakReference<NotifyActionActivity> sPopActionRefer = null;
    private static final LaunchStart launchStart = new LaunchStart();
    public static final String TAG = NotifyInitProvider.TAG;

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener touch = (view, motionEvent) -> {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            finish();
        }
        return false;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnTouchListener(touch);
        view.setBackgroundColor(Color.TRANSPARENT);

        setContentView(view);
        sPopActionRefer = new WeakReference<>(this);
        launchStart.cancel();
        Log.d(TAG, "NotifyActionActivity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "NotifyActionActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "NotifyActionActivity onDestroy");
    }

    public static void actionStart(Context context) {
        Log.i(TAG, "NotifyActionActivity actionStart");
        destroy();
        try {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(context, NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyActionAlias);
            intent.setComponent(componentName);
//            Intent intent = new Intent(context, NotifyActionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchStart.doStart(context, intent);
        } catch (Throwable t) {
            Intent intent = new Intent(context, NotifyActionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchStart.doStart(context, intent);
        }
    }


    public static void destroy() {
        Log.i(TAG, "NotifyActionActivity destroy");
        Activity activity = sPopActionRefer != null ? sPopActionRefer.get() : null;
        if (activity != null && !activity.isDestroyed()) {
            Log.i(TAG, "NotifyActionActivity finish");
            activity.finish();
        }
    }

}
