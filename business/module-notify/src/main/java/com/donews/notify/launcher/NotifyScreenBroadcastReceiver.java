package com.donews.notify.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifyScreenBroadcastReceiver extends BroadcastReceiver {
    private final NotifyScreenDelegate delegate = new NotifyScreenDelegate();

    @Override
    public void onReceive(Context context, Intent intent) {
        delegate.onReceive(context, intent);
    }
}
