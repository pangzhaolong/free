package com.donews.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class ScreenStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(Intent.ACTION_SCREEN_ON, intent.getAction()) || Objects.equals(Intent.ACTION_USER_PRESENT,
                intent.getAction())) {
            action = intent.getAction();
            DazzleActivity.destroyOnePixelActivity();
            process = false;
        } else if (Objects.equals(Intent.ACTION_SCREEN_OFF, intent.getAction())) {
            // 对应前台进程，通常不会被杀
            action = intent.getAction();
            if (!process) {
                DazzleActivity.showOnePixelActivity(context);
                process = true;
            }
        }
    }

    private static boolean process = false;
    static String action = "";
}
