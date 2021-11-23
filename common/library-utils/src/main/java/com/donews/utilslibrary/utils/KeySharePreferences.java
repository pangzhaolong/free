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

    public final static String CLOSE_RED_PACKAGE_COUNTS = "close_red_package_counts";

    public final static String STEPS_TO_GOLD_RED_PACKAGE_COUNTS = "steps_to_gold_red_package_counts";

    public final static String SHOW_DIALOG_WHEN_LAUNCH = "show_dialog_when_launch";


    public final static String INTO_FRONT_COUNTS = "into_front_counts";


    public final static String HAS_DO_INTO_FRONT = "has_do_into_front";

    public final static String LOTTERY_COUNTS = "lottery_counts";

    /** main模块。报错首页提示浮标最新一期提交的记录。表示当前期数已经被记录过了 */
    public final static String MAIN_MASK_FLG = "main_model_mask_flg";

    public final static String IS_FIRST_IN_APP = "is_first_in_app";
}
