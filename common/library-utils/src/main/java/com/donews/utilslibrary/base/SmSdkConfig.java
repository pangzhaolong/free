package com.donews.utilslibrary.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.donews.utilslibrary.utils.KeyConstant;
import com.ishumei.smantifraud.SmAntiFraud;

/**
 * @Author: honeylife
 * @CreateDate: 2020/9/27 10:59
 * @Description:
 */
public class SmSdkConfig {
    public static void initData(Application application) {
        // 如果 AndroidManifest.xml 中没有指定主进程名字，主进程名默认与 packagename 相同
        // 如下条件判断保证只在主进程中初始化 SDK
        if (application.getPackageName().equals(getCurProcessName(application))) {
            SmAntiFraud.SmOption option = new SmAntiFraud.SmOption();
            //1.通用配置项
            option.setOrganization(KeyConstant.getOrganization()); //必填，组织标识，邮件中 organization 项
            option.setAppId(KeyConstant.getShuMeiAppId()); //必填，应用标识，登录数美后台应用管理查看
            option.setPublicKey(KeyConstant.getShuMeiKey()); //必填，加密 KEY，邮件中 android_public_key 附件内容
            option.setAinfoKey(KeyConstant.getShuMeiKeyInfo()); //必填，加密 KEY，邮件中 Android ainfo key 项
            // 2 连接机房特殊配置项
            // 2.1 业务机房在国内
            // 1) 用户分布：中国（默认设置）
            option.setArea(SmAntiFraud.AREA_BJ);
            //3.SDK 初始化
            SmAntiFraud.create(application, option);
        }

    }

    private static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    /**
     * getDeviceId 调用时机：
     * SmAntiFraud.getDeviceId()接口在真正需要 DeviceId 时再进行调用。
     * 不要在 create 后立即调用，也不要缓存调用 getDeviceId 的结果，
     * deviceId 在 sdk 内部会做缓存和更新处理。
     */
}
