package com.donews.keepalive;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

public class DeviceHelper {

    //获取屏幕状态，0->锁屏了, 1->未锁屏, 2-> 正处于解锁状态
    public static int getLockState(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            //a、未锁屏 b、目前正处于解锁状态
        } else {
            //锁屏了
            return 0;
        }

        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            //a、屏幕是黑的 b、目前正处于解锁状态 。
        } else {
            return 1;
        }
        return 2;
    }

    /***String:--es, int:--ei, boolean:--ez*/
    public static String intentToCmd(Intent intent) {
        StringBuilder paramsCmd = new StringBuilder();
        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (bundle.get(key) != null) {
                        Object value = bundle.get(key);
                        if (value != null) {
                            if (value instanceof String) {
                                if (String.valueOf(value) != null && !String.valueOf(value).isEmpty())
                                    paramsCmd.append(" --es ").append(key).append(" ").append(value);
                            } else if (value instanceof Integer) {
                                paramsCmd.append(" --ei ").append(key).append(" ").append(value);
                            } else if (value instanceof Boolean) {
                                paramsCmd.append(" --ez ").append(key).append(" ").append(value);
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return paramsCmd.toString();
    }

    /**
     * 判断是否是小米
     *
     * @return true：是；false:不是
     */
    public static boolean isXiaoMi() {
        try {
            String brand = Build.BRAND;
            if (brand != null && !brand.isEmpty()) {
                if (brand.equalsIgnoreCase("XIAOMI")) {
                    return true;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }
}
