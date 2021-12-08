package com.donews.jdd;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.DNEventBusUtils;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.CrashHandlerUtil;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.common.config.ModuleLifecycleConfig;
import com.donews.keepalive.global.KeepAliveAPI;
import com.donews.notify.launcher.NotificationCreate;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.utils.KeyConstant;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.Utils;
import com.donews.web.base.WebConfig;
import com.keepalive.daemon.core.utils.NotificationUtil;
import com.module_lottery.BuildConfig;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/10 17:22<br>
 * 版本：V1.0<br>
 */
public class MyApplication extends BaseApplication {
    private static final String notifyChannelId = "JDD-CHANNEL-RED";
    //极端情况下，通知栏显示下方通知
    private static final String notifyTitle = "【待签收】";
    private static final String notifyText = "派送中...已向您的账号转入一份神秘礼物，点击查收";

    private final AtomicBoolean keepAliveInstall = new AtomicBoolean(false);

    private static final String TAG = "Application-Main";


    @Override
    public void onCreate() {
        super.onCreate();
        final boolean isMainProcess = Utils.isMainProcess(this);

        if (isMainProcess) {
            if (LogUtil.allow) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                ARouter.openLog();     // 打印日志
                ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            }
            ARouter.init(this); // 尽可能早，推荐在Application中初始化

            // 初始化需要初始化的组件
            ModuleLifecycleConfig.getInstance().initModuleAhead(this);

            //错误日志收集
            CrashHandlerUtil.getInstance().init(this);
            // 集成bugly
            CrashReport.initCrashReport(getApplicationContext(), KeyConstant.getBuglyId(), BuildConfig.DEBUG);

            DNEventBusUtils.INSTANCE.init(MyApplication.this);

            WebConfig.init(MyApplication.this);

            disableAPIDialog();

            try {
                //主进程获得监听
                NotifyLuncherConfigManager.getInstance().addAppConfigDataUpdateListener(update -> {
                    installKeepAlive(isMainProcess);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //其余进程初始化keepalive模块
        if (NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().keepAliveOpen) {
            Log.d(TAG, "install local KeepAlive, pid =" + Process.myPid());
            installKeepAlive(isMainProcess);
        }
    }

    private void installKeepAlive(boolean isMainProcess) {
        boolean keepAliveOpen = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().keepAliveOpen;
        int keepAliveMinVersion = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().keepAliveMinVersion;
        Log.i(TAG, "installKeepAlive , keepAliveOpen = " + keepAliveOpen + ",keepAliveMinVersion = " + keepAliveMinVersion);
        if (!keepAliveInstall.get()) { //如果保活模块未安装
            if (keepAliveOpen && Build.VERSION.SDK_INT >= keepAliveMinVersion) {//线上配置开启该模块
                Notification notification = NotificationCreate.createNotification(this, notifyChannelId, getString(R.string.app_name), R.drawable.ic_launcher_round, notifyTitle, notifyText);
                if (notification == null) {
                    notification = NotificationUtil.createDefaultNotification(this);
                }

                KeepAliveAPI.start(this, notification);
                if (isMainProcess) {
                    Log.w(TAG, "install keepalive Dazzle , mainProcess");
                    try {
                        AnalysisUtils.onEvent(this, AnalysisParam.NOTICE_BAR_DISPLAY);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
                keepAliveInstall.set(true);
            }
        }
    }


    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // dex突破65535的限制
        MultiDex.install(this);
    }
}
