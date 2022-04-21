package com.donews.notify.launcher.utils;

import android.util.Log;

import com.donews.base.BuildConfig;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;

import java.util.List;

/**
 * @author lcl
 * Date on 2022/1/20
 * Description:
 * 日志输出
 */
public class NotifyLog {

    private static String tag = "notifyDes";

    private static String tagBar = "notifyBar";
    //是否显示Toast提示
    private static boolean isToast = BuildConfig.DEBUG;

    public static void log(String msg) {
        Log.e(tag, msg);
        if (isToast) {
            ToastUtil.showShort(BaseApplication.getInstance(), msg);
        }
    }

    /**
     * 通知栏通知的日志输出
     *
     * @param msg
     */
    public static void logBar(String msg) {
        Log.e(tagBar, "[通知栏通知]:" + msg);
        if (isToast) {
            ToastUtil.showShort(BaseApplication.getInstance(), msg);
        }
    }

    public static void logBarNotToast(String fixFlg, List msg) {
        StringBuffer sb = new StringBuffer("\n");
        for (int i = 0; i < msg.size(); i++) {
            sb.append("列表结果" + i + " => " + msg.get(i) + "\n");
        }
        Log.e(tagBar, fixFlg + ":" + sb.toString());
    }

    public static void logNotToast(String msg) {
        Log.e(tag, msg);
    }

    public static void logBarNotToast(String msg) {
        Log.e(tagBar, msg);
    }

    public static void logNotToast(String fixFlg, List msg) {
        StringBuffer sb = new StringBuffer("\n");
        for (int i = 0; i < msg.size(); i++) {
            sb.append("列表结果" + i + " => " + msg.get(i) + "\n");
        }
        Log.e(tag, fixFlg + ":" + sb.toString());
    }
}
