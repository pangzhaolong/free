package com.donews.utilslibrary.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author: honeylife
 * @CreateDate: 2020/10/30 15:03
 * @Description:
 */
public class Utils {

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        try {
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for(ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName;
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (!TextUtils.isEmpty(processName)){
            return processName;
        }

        //第二种保底获取process方法
        BufferedReader reader = null;
        try {
            FileReader fileReader = new FileReader("/proc/" + android.os.Process.myPid() + "/cmdline");
            reader = new BufferedReader(fileReader);
            String process = reader.readLine();
            if (!TextUtils.isEmpty(process)) {
                process = process.trim();
            }
            processName = process;
        } catch (Throwable ignored) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {
            }
        }

        if (!TextUtils.isEmpty(processName)){
            return processName;
        }

        //第三种获取process方法
        BufferedReader br = null;
        try {
            File file = new File("/proc/self/cmdline");
            br = new BufferedReader(new FileReader(file));
            String process = br.readLine();
            if (!TextUtils.isEmpty(process)) {
                process = process.trim();
            }
            processName = process;
        } catch (Exception ignored) {

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ignored) {
            }
        }
        if (!TextUtils.isEmpty(processName)){
            return processName;
        }
        //高版本API保底
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            processName = Application.getProcessName();

            if (!TextUtils.isEmpty(processName)){
                return processName;
            }
        }
        return null;
    }

    /**
     * @return true 表示为主进程
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getCurProcessName(context));
    }
}
