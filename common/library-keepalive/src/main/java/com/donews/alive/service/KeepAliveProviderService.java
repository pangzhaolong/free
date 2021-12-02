package com.donews.alive.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.alive.KeepAlive;
import com.donews.alive.provider.IKeepAliveProvider;
import com.keepalive.daemon.core.notification.NotifyResidentService;
import java.util.List;

/**
 * 保活service，专门调用 IKeepAliveProviders,实现保活和具体业务逻辑的解耦
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/13 13:25
 */
public class KeepAliveProviderService extends NotifyResidentService {

    @Override
    public void doStart() {
        super.doStart();
        List<String> mIKeepAliveProviderPathList = KeepAlive.getInstance().getIKeepAliveProviderPathList();
        for (String path : mIKeepAliveProviderPathList) {
            IKeepAliveProvider provider = (IKeepAliveProvider) ARouter.getInstance().build(path).navigation();
            if (provider != null) {
                provider.doStart(this);
            }
        }
    }

    @Override
    public void doStartCommand(Intent intent, int i, int i1) {
        super.doStartCommand(intent, i, i1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel("39223", "今日福利派送中", NotificationManager.IMPORTANCE_DEFAULT));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "39223");
            startForeground(39223, builder.build());
        }
        List<String> mIKeepAliveProviderPathList = KeepAlive.getInstance().getIKeepAliveProviderPathList();
        for (String path : mIKeepAliveProviderPathList) {
            IKeepAliveProvider provider = (IKeepAliveProvider) ARouter.getInstance().build(path).navigation();
            if (provider != null) {
                provider.doStartCommand(this, intent, i, i1);
            }
        }
    }

    @Override
    public void doRelease() {
        super.doRelease();
        List<String> mIKeepAliveProviderPathList = KeepAlive.getInstance().getIKeepAliveProviderPathList();
        for (String path : mIKeepAliveProviderPathList) {
            IKeepAliveProvider provider = (IKeepAliveProvider) ARouter.getInstance().build(path).navigation();
            if (provider != null) {
                provider.doRelease(this);
            }
        }
    }
}
