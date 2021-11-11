package com.donews.utilslibrary.utils;

import com.tencent.mmkv.MMKV;

/**
 * 用于判断当前app是否在前台等
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 16:07
 */
public class AppStatusUtils {

    public static final String MMKV_NAME = "app_status";
    public static final String APP_STATE_FORCE_GROUND = "app_force_ground";

    public static final String MMKV_APP_INSTALL_TIME = "app_install_time";
    public static final String MMKV_APP_INSTALL_FLAG = "app_install_flag";

    private static MMKV mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE);

    public static boolean isForceGround() {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE);
        }
        if (mmkv != null) {
            return mmkv.decodeBool(APP_STATE_FORCE_GROUND, false);
        } else {
            return false;
        }
    }

    public static void setAppForceGround(boolean forceGround) {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE);
        }
        if (mmkv != null) {
            mmkv.encode(APP_STATE_FORCE_GROUND, forceGround);
        }
    }


    public static void saveAppInstallTime() {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE);
        }
        if (mmkv != null) {
            boolean firstOpenApp = mmkv.decodeBool(MMKV_APP_INSTALL_FLAG, true);
            if (firstOpenApp) {
                mmkv.encode(MMKV_APP_INSTALL_TIME, System.currentTimeMillis());
                mmkv.encode(MMKV_APP_INSTALL_FLAG, true);
            }
        }
    }


    public static long getAppInstallTime() {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE);
        }
        if (mmkv != null) {
            return mmkv.decodeLong(MMKV_APP_INSTALL_TIME, 0L);
        } else {
            return 0L;
        }
    }
}
