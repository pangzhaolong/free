package com.donews.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class SystemNotifyReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (hasAction(intent.getAction())) {
            if (!JobHandlerService.isServiceRunning(context, DazzleService.class.getName())&& DazzleReal.serviceStart) {
                if (Build.VERSION.SDK_INT < 26) {
                    Intent localIntent = new Intent(context, DazzleService.class);
                    localIntent.putExtra("type", "3");
                    try {
                        context.startService(localIntent);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean hasAction(String action) {
        for (String stateIntent : DazzleReal.STATE_INTENTS) {
            if (Objects.equals(stateIntent, action)) return true;
        }
        return false;
    }
}
