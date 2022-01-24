package com.donews.notify.launcher.utils;

import static com.donews.notify.launcher.NotifyScreenDelegate.isLockScreen;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.donews.base.base.BaseApplication;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.launcher.NotifyScreenDelegate;

/**
 * @author lcl
 * Date on 2022/1/7
 * Description:
 * 桌面通知的前台检查，为了适配在后台一定时间主动激活通知
 */
public class AppNotifyForegroundUtils {

    /**
     * 上一次，应用退到后台的时间: <0:不需要检查，>0：正常的检查
     */
    private static long exitBackgroundTime = -1;
    /**
     * 应用状态变化
     */
    private static Utils.OnAppStatusChangedListener statusChangedListener;
    /**
     * 时间更新的广播
     */
    private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(intent.ACTION_TIME_TICK)) {
                if (exitBackgroundTime < 0) {
                    return;//不检查后台
                }
                if(isLockScreen){
                    return;//已经锁屏状态。不在继续处理
                }
                long delayTime = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyBackgroundShowTime * 1000L;
                if (delayTime <= 0) {
                    return;//未设置。不生效
                }
                boolean isSendNotify = System.currentTimeMillis() - exitBackgroundTime >= delayTime;
                NotifyLog.logNotToast("(后台)时间更新了...");
                if (isSendNotify) {
                    NotifyLog.logNotToast("(后台)满足发送后台通知的逻辑");
                    //超过了设置的时间。显示通知
                    //正常的限制条件弹出
                    new NotifyScreenDelegate().showNotify(BaseApplication.getInstance());
                    //直接强制弹出。不经过条件限制
//                    NotifyActivity.actionStart(context);
                    exitBackgroundTime = System.currentTimeMillis();
                }
            }
        }
    };

    /**
     * 刷新后台时间
     */
    public static void updateBackgroundTime(){
        exitBackgroundTime = System.currentTimeMillis();
    }

    /**
     * 开启前台检查。当切换到后台达到一定时间后。将激活桌面通知（主要覆盖场景是在后台+屏幕常亮情况）
     * 注意:此通知依然遵循中台主配置+分控配置约束
     */
    public static void startForegroundCheck() {
        if (statusChangedListener != null) {
            return;//已经初始化过了
        }
        exitBackgroundTime = System.currentTimeMillis();
        if (statusChangedListener == null) {
            //注册广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            BaseApplication.getInstance().registerReceiver(broadcastReceiver, filter);
            //注册应用状态监听
            statusChangedListener = new Utils.OnAppStatusChangedListener() {
                @Override
                public void onForeground(Activity activity) {
//                    if (activity.getClass().getName().equals(DazzleActivity.class.getName()) ||
//                            activity.getClass().getName().equals(NotifyActivity.class.getName())) {
//                        return;//过滤掉一像素页面和桌面通知页面
//                    }
                    exitBackgroundTime = -1;
                }

                @Override
                public void onBackground(Activity activity) {
                    exitBackgroundTime = System.currentTimeMillis();
                }
            };
            AppUtils.registerAppStatusChangedListener(statusChangedListener);
        }
    }
}
