package com.donews.jpush.utils;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

public class JPushSwitch {

    /**
     * 返回激光推送开关的状态
     */
    public static boolean getSwitchType(Context context) {
        if (context != null) {
            return JPushInterface.isPushStopped(context.getApplicationContext());
        }
        return false;
    }

    /**
     * 关闭激光推送
     */
    public static void stopPush(Context context) {
        if (context != null) {
             JPushInterface.stopPush(context.getApplicationContext());
        }
    }
    /**
     * 重启激光推送
     */
    public static void resumePush(Context context) {
        if (context != null) {
            JPushInterface.resumePush(context.getApplicationContext());
        }
    }


}
