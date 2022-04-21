package com.donews.notify.launcher.notifybar.uistyle;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.provider.Settings;

import com.donews.notify.R;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.notifybar.NotifyBarShowManager;
import com.donews.notify.launcher.utils.NotifyLog;
import com.ycbjie.notificationlib.NotificationParams;
import com.ycbjie.notificationlib.NotificationUtils;

/**
 * @author lcl
 * Date on 2022/3/10
 * Description:
 * 样式0的显示处理模式
 */
public class UIStyle0ShowImpl implements IUIStyleShow {

    NotificationUtils notificationUtils = null;
    private int count = 0;

    @Override
    public void showNotify(Context context, NotifyBarDataConfig.NotifyBarUIDataConfig data) {
        NotifyLog.logBar("开始处理显示UI样式[" + data.uiType + "]的通知栏数据");
        //这三个属性是必须要的，否则异常
        notificationUtils = new NotificationUtils(
                context, NotifyBarShowManager.getChannelId(), NotifyBarShowManager.getChannelName());

        NotificationParams params = notificationUtils.getNotificationParams()
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setTicker(data.title)
                //设置优先级
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(PendingIntent.getActivity(context, 0, getJmupIntent(data), 0))
                //自定义震动效果
                .setFlags(Notification.FLAG_AUTO_CANCEL);
        notificationUtils.setNotificationParams(params);
//        notificationUtils.setNotificationParams(notificationUtils.getNotificationParams())
        Notification notification = notificationUtils.getNotification(
                data.title, data.desc, getAppIcon());
        notificationUtils.getManager().notify(NotifyBarShowManager.getNotifyId(data.uiType), notification);
        count++;
        //开始显示
        NotifyLog.logBar("显示UI样式[" + data.uiType + "]成功处理完成了");
    }
}
