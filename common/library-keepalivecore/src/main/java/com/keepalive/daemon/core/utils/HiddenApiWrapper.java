package com.keepalive.daemon.core.utils;

import android.util.Log;

import com.donews.crashhandler.reflect.ReflectionLimit;

import java.lang.reflect.Method;

public class HiddenApiWrapper {

    public static boolean exemptAll() {
        Log.i("HiddenApiWrapper", "Start execute exemptAll method ...");
        return ReflectionLimit.clearLimit();
    }
}
