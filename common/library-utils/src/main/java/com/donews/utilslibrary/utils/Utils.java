package com.donews.utilslibrary.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

/**
 * @Author: honeylife
 * @CreateDate: 2020/10/30 15:03
 * @Description:
 */
public class Utils {

    private static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * @return true 表示为主进程
     */
    public static boolean getMainProcess(Application application) {
        return application.getPackageName().equals(getCurProcessName(application));
    }
}
