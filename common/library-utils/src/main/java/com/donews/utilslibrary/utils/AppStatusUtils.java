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

    public static boolean isForceGround() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_NAME);
        if (mmkv != null) {
            return mmkv.decodeBool(APP_STATE_FORCE_GROUND, false);
        } else {
            return false;
        }
    }

    public static void setAppForceGround(boolean forceGround) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_NAME);
        if (mmkv != null) {
            mmkv.encode(APP_STATE_FORCE_GROUND, forceGround);
        }
    }

}
