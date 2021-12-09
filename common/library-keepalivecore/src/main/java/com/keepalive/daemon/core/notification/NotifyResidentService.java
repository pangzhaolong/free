package com.keepalive.daemon.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Process;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.keepalive.daemon.core.Constants;
import com.keepalive.daemon.core.DaemonHolder;
import com.keepalive.daemon.core.KeepAliveService;
import com.keepalive.daemon.core.component.DaemonService;
import com.keepalive.daemon.core.utils.Logger;
import com.keepalive.daemon.core.utils.NotificationUtil;
import com.keepalive.daemon.core.utils.ServiceHolder;

public class NotifyResidentService extends KeepAliveService {

    @Override
    public final void onCreate() {
        doStart();
        super.onCreate();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(Logger.TAG, "NotifyResidentService  onStartCommand " +"intent: " + intent + ", startId: " + startId+",pid="+ Process.myPid());

        doStartCommand(intent, flags, startId);

        Notification noti = DaemonHolder.getGlobalNotifycation();
        if(noti == null){
            Logger.w(Logger.TAG, "Notification  is default");
            noti = NotificationUtil.createNotification(
                    this,
                    intent.getIntExtra(Constants.NOTI_SMALL_ICON_ID, 0),
                    intent.getIntExtra(Constants.NOTI_LARGE_ICON_ID, 0),
                    intent.getStringExtra(Constants.NOTI_TITLE),
                    intent.getStringExtra(Constants.NOTI_TEXT),
                    intent.getBooleanExtra(Constants.NOTI_ONGOING, true),
                    intent.getIntExtra(Constants.NOTI_PRIORITY, NotificationCompat.PRIORITY_DEFAULT),
                    intent.getIntExtra(Constants.NOTI_IMPORTANCE, NotificationManager.IMPORTANCE_DEFAULT),
                    intent.getStringExtra(Constants.NOTI_TICKER_TEXT),
                    (PendingIntent) intent.getParcelableExtra(Constants.NOTI_PENDING_INTENT),
                    (RemoteViews) intent.getParcelableExtra(Constants.NOTI_REMOTE_VIEWS)
            );
        }

        NotificationUtil.showNotification(this, noti);

        ServiceHolder.getInstance().bindService(this, DaemonService.class, null);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public final void onDestroy() {
        doRelease();
        super.onDestroy();
    }

    protected void doStart() {
    }

    protected void doStartCommand(Intent intent, int flags, int startId) {
    }

    protected void doRelease() {
    }
}
