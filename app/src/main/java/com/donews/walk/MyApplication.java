package com.donews.walk;

import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.DNEventBusUtils;
import com.dn.sdk.sdk.AdSdkManager;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.CrashHandlerUtil;
import com.donews.common.config.ModuleLifecycleConfig;
import com.donews.utilslibrary.utils.KeyConstant;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.Utils;
import com.donews.web.base.WebConfig;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/10 17:22<br>
 * 版本：V1.0<br>
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        if (Utils.getMainProcess(this)) {
            DNEventBusUtils.INSTANCE.init(this);
            if (LogUtil.allow) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                ARouter.openLog();     // 打印日志
                ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            }
            ARouter.init(this); // 尽可能早，推荐在Application中初始化
            // 初始化需要初始化的组件
            ModuleLifecycleConfig.getInstance().initModuleAhead(this);

            CrashHandlerUtil.getInstance().init(this);

            // 集成bugly
            CrashReport.initCrashReport(getApplicationContext(), KeyConstant.getBuglyId(), BuildConfig.DEBUG);

            WebConfig.init(this);
            disableAPIDialog();
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
