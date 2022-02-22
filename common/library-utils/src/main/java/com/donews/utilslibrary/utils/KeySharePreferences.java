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

    // 用户创建时间
    public final static String USER_REGISTER_TIME = "user_register_time";

    // 当前提现积分任务完成的次数
    public final static String CURRENT_SCORE_TASK_COUNT = "current_score_task_count";

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
    public final static String OPENED_RED_PACKAGE_COUNTS = "opened_red_package_counts";

    public final static String STEPS_TO_GOLD_RED_PACKAGE_COUNTS = "steps_to_gold_red_package_counts";
    //系统时间
    public final static String TIME_SERVICE = "TIME_SERVICE";
    /**
     * 通知弹窗随机的红包金额
     */
    public final static String NOTIFY_RANDOM_RED_AMOUNT = "NOTIFY_RANDOM_RED_AMOUNT";

    //第一个红包是否开启
    public final static String FIRST_RP_IS_OPEN = "first_rp_is_open";
    public final static String FIRST_RP_OPEN_PRE_ID = "first_rp_open_pre_id";
    public final static String FIRST_RP_OPEN_PRE_SCORE = "first_rp_open_pre_score";
    public final static String FIRST_RP_CAN_OPEN = "first_rp_can_open";

    //积分墙次留任务
    public final static String RETENTION_TASK_SRC_REQUEST_ID = "retention_task_src_request_id";
    public final static String RETENTION_TASK_WALL_REQUEST_ID = "retention_task_wall_request_id";



    public final static String SHOW_DIALOG_WHEN_LAUNCH = "show_dialog_when_launch";


    public final static String INTO_FRONT_COUNTS = "into_front_counts";


    public final static String HAS_DO_INTO_FRONT = "has_do_into_front";

    public final static String LOTTERY_COUNTS = "lottery_counts";

    /**
     * main模块。报错首页提示浮标最新一期提交的记录。表示当前期数已经被记录过了
     */
    public final static String MAIN_MASK_FLG = "main_model_mask_flg";

    public final static String IS_FIRST_IN_APP = "is_first_in_app";

    /**
     * notify限制
     */
    public final static String KEY_NOTIFYOPEN_TIME = "key_notifyopen_time";

    public final static String KEY_NOTIFYCOUNT_LIMIT = "key_notifycount_limit";

}
