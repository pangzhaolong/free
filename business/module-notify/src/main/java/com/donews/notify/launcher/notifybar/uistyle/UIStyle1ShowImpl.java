package com.donews.notify.launcher.notifybar.uistyle;

import static android.app.Notification.FLAG_ONGOING_EVENT;
import static android.app.Notification.VISIBILITY_SECRET;
import static com.blankj.utilcode.util.NotificationUtils.IMPORTANCE_HIGH;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.donews.base.utils.glide.GlideUtils;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyActivity;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.notifybar.NotifyBarManager;
import com.donews.notify.launcher.notifybar.NotifyBarShowManager;
import com.donews.notify.launcher.notifybar.listeners.OnLoadUrlImageListener;
import com.donews.notify.launcher.notifybar.listeners.OnNotificationBuiderCreateListener;
import com.donews.notify.launcher.utils.NotifyLog;
import com.ycbjie.notificationlib.NotificationParams;
import com.ycbjie.notificationlib.NotificationUtils;

/**
 * @author lcl
 * Date on 2022/3/10
 * Description:
 * 样式0的显示处理模式
 * <p>
 * 给单个渠道授权的方式：
 * Intent intent = new Intent();
 * if (Build.VERSION.SDK_INT >= 26) {
 * // android8.0单个channelid设置
 * intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
 * intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
 * intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
 * } else {
 * // android 5.0以上一起设置
 * intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
 * intent.putExtra("app_package", getPackageName());
 * intent.putExtra("app_uid", getApplicationInfo().uid);
 * }
 * startActivity(intent);
 */
public class UIStyle1ShowImpl implements IUIStyleShow {

    NotificationUtils notificationUtils = null;
    private Context context;
    private NotifyBarDataConfig.NotifyBarUIDataConfig data;
    RemoteViews remoteViews = null;

    @Override
    public void showNotify(Context context, NotifyBarDataConfig.NotifyBarUIDataConfig data) {
        NotifyLog.logBar("开始处理显示UI样式[" + data.uiType + "]的通知栏数据");
        this.context = context;
        this.data = data;
        OnLoadUrlImageListener loadImage = bitmap -> {
            //这三个属性是必须要的，否则异常
            notificationUtils = new NotificationUtils(
                    context, NotifyBarShowManager.getChannelId(), NotifyBarShowManager.getChannelName());
//            int showMode = NotifyBarManager.Ins().getNotifyBarConfigBean().notifyShowModel;
//            if (remoteViews != null && showMode == 1) {
//                bindData(data, bitmap);
//                NotifyLog.logBar("已经存在。只更新[" + data.uiType + "]的通知栏数据");
//                return;//更新模式,直接更新数据
//            }
            setNotifyCannelAttr();
            setNotifyParams(context, data, bitmap);
            Notification notification = notificationUtils.getNotification(
                    data.title, data.desc, getAppIcon());
            int notifyId = NotifyBarShowManager.getNotifyId(data.uiType);
            notificationUtils.getManager()
                    .notify(notifyId, notification);
            NotifyLog.logBar("显示UI样式[" + data.uiType + "]成功处理完成了");
        };
        loadUrlImg(data.rightIcon, loadImage);
    }

    @Override
    public void buildBuider(String tag, Notification.Builder builder) {
        if (!tag.equals(notificationUtils.toString())) {
            return;//不是当前的对象
        }
        NotifyLog.logBar("显示UI样式[" + data.uiType + "],构建原始Builder参数");
    }

    @Override
    public void buildCompatBuider(String tag, NotificationCompat.Builder builder) {
        if (!tag.equals(notificationUtils.toString())) {
            return;//不是当前的对象
        }
        NotifyLog.logBar("显示UI样式[" + data.uiType + "],构建原始Builder参数");
    }

    @SuppressLint("WrongConstant")
    private void setNotifyCannelAttr() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationUtils.getNotificationChannel()
                    .setImportance(IMPORTANCE_HIGH);
            NotificationChannel channel = notificationUtils.getNotificationChannel();
            channel.canBypassDnd();//是否绕过请勿打扰模式
            channel.enableLights(true);//闪光灯
            channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
            channel.setLightColor(Color.RED);//闪关灯的灯光颜色
            channel.canShowBadge();//桌面launcher的消息角标
            channel.enableVibration(true);//是否允许震动
//            channel.getAudioAttributes();//获取系统通知响铃声音的配置
//            channel.getGroup();//获取通知取到组
//            channel.setBypassDnd(true);//设置可绕过 请勿打扰模式
//            channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
//            channel.shouldShowLights();//是否会有灯光

            //判断是否开启了静默通知
//            if (notificationUtils.isNoImportance(channel)) {
//                //跳转设置中心
//                notificationUtils.openChannelSetting(channel);
//            }
        }
    }

    //设置通知参数
    @SuppressLint("RemoteViewLayout")
    private void setNotifyParams(
            Context context, NotifyBarDataConfig.NotifyBarUIDataConfig data, Bitmap res) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notify_bar_item_type1);
        bindData(data, res);
        NotificationParams params = notificationUtils.getNotificationParams()
                .setContent(remoteViews)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setTicker(data.title)
                //设置优先级
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(
                        PendingIntent.getActivity(context, 0, getJmupIntent(data), 0)
                )
                //自定义震动效果
                .setFlags(Notification.FLAG_AUTO_CANCEL);
        notificationUtils.setNotificationParams(params);
    }

    /**
     * 绑定数据
     *
     * @param data
     * @param res
     */
    public void bindData(NotifyBarDataConfig.NotifyBarUIDataConfig data, Bitmap res) {
        remoteViews.setTextViewText(R.id.bar_title, data.title);
        remoteViews.setTextViewText(R.id.bar_title_desc, data.desc);
        if (res != null) {
            remoteViews.setImageViewBitmap(R.id.bar_right_icon, res);
        }
    }
}
