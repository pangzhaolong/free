package com.donews.keepalive;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;

public class CommonNotification {
    public static void startForeground(Service service) {
        if (Build.VERSION.SDK_INT >= 26) {
            Intent intent =new Intent(service.getApplicationContext(), NotificationClickReceiver.class);
            intent.setAction(NotificationClickReceiver.ACTION_CLICK_NOTIFICATION);
            Notification notification;
            if (DazzleReal.notification != null) {
                notification = DazzleReal.notification;
                if (DazzleReal.id != 0) {
                    service.startForeground(DazzleReal.id, notification);
                }
            }
        }
    }
}
