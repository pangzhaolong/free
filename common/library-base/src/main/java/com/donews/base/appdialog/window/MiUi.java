package com.donews.base.appdialog.window;

import android.os.Build;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public class MiUi {
    public static boolean rom() {
        return Build.MANUFACTURER.equals("Xiaomi");
    }
}
