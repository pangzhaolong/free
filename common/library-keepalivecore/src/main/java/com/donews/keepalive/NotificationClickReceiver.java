package com.donews.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class NotificationClickReceiver extends BroadcastReceiver {

    public static final String ACTION_CLICK_NOTIFICATION = "CLICK_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), ACTION_CLICK_NOTIFICATION)) {
            //回调方法
            if (DazzleReal.foregroundNotificationClickListener != null) {
                DazzleReal.foregroundNotificationClickListener.foregroundNotificationClick(context, intent);
            }
        }
    }
}
