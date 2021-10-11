package com.donews.utilslibrary.utils;

import com.donews.utilslibrary.BuildConfig;

/**
 * <p> sp 数据存储的key</p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 17:19<br>
 * 版本：V1.0<br>
 */
public class KeySharePreferences {
    //本地sp存储目录
    public final static String SP_KEY = BuildConfig.APP_IDENTIFICATION + "_sp";

    //本地的userInfo对象
    public final static String USER_INFO = "user_info";
    // 本地token
    public final static String TOKEN = "token";
    //本地用户的userId
    public final static String USER_ID = "userId";
    //oaid
    public final static String OAID = "share_util_oaid";
    // 是否给了协议权限
    public final static String AGREEMENT = "agreement_first";

    //新老用户标记
    public final static String USER_TAG = "user_tag";


    public final static String SPLASH_BACKGROUND_INTERVAL_TIME = "splash_bg_interval";

    // 用户首次协议
    public final static String DEAL = "main_agree_deal";
    /**
     * app全局Crash配置
     */
    public final static String APP_GLOBAL_CRASH_CONFIG = "app_global_crash_config";
}
