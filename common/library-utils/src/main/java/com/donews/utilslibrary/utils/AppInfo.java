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

    /**
     * 保存微信登录的Code,此code是判断是否登录、重新自动登录的标志
     * @param code
     */
    public static void saveWXLoginCode(String code){
        SPUtils.setInformain("wxLoginCode",code);
    }

    /**
     * 获取保存的微信登录凭证。
     * @return null 或者 “” 表示未登录微信
     */
    public static String getWXLoginCode(){
        return SPUtils.getInformain("wxLoginCode",null);
    }

    /**
     * 判断是否微信登录了。此才是用户登录的标志
     * @return T:表示已经登录，F:表示未登录
     */
    public static boolean checkIsWXLogin(){
        String code = getWXLoginCode();
        return code != null && code.length() > 0;
    }

}
