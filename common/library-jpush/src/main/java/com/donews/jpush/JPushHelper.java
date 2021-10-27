package com.donews.jpush;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

public class JPushHelper {
    public static void setDebugMode(boolean debugMode) {
        JPushInterface.setDebugMode(debugMode);
    }
    public static void init(Context context) {
        JPushInterface.init(context);
    }
}
