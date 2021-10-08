package com.donews.utilslibrary.utils;

import android.text.TextUtils;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/17 10:46<br>
 * 版本：V1.0<br>
 */
public class AppInfo {
    public static String getToken(String token) {
        String tokenStr = !TextUtils.isEmpty(token) ? token : SPUtils.getInformain(KeySharePreferences.TOKEN, "");
        if (!TextUtils.isEmpty(tokenStr) && tokenStr.startsWith("Bearer")) {
            return tokenStr;
        } else
            return "Bearer " + tokenStr;
    }

    public static String getToken() {
        return SPUtils.getInformain(KeySharePreferences.TOKEN, "");
    }

    public static String getUserId() {
        return SPUtils.getInformain(KeySharePreferences.USER_ID, "");
    }

}
