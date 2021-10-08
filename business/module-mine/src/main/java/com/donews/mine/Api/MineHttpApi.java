package com.donews.mine.Api;

import com.donews.mine.BuildConfig;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/18 14:42<br>
 * 版本：V1.0<br>
 */
public class MineHttpApi {

    private static final String USER_URL = BuildConfig.LOGIN_URL + "share/v1/";
    private static final String TASK_URL = BuildConfig.TASK_URL + "score/";
    public static final String QUERY = TASK_URL + "query";
    // /code 获取邀请码 get // 填写邀请码 put请求
    public static final String CODE = USER_URL + "code";

    // h5
    public static final String H5_URL = BuildConfig.HTTP_H5;
    //邀请好友
    public static final String INVITATION_URL = H5_URL + "Invitation";

    //提现
    public static final String CASH = H5_URL + "cash";

    //钱包
    public static final String WALLET = H5_URL + "wallet";

    //客户服务
    public static final String CUSTOMER = H5_URL + "customer";

    /**
     * 签到提醒
     */
    public static final String SIGN_REMIND = BuildConfig.TASK_URL + "sign/remind";

    /**
     * 用户签到列表查询
     */
    public static final String SIGN_QUERY = BuildConfig.TASK_URL + "sign/query";

    /**
     * 用户签到
     */
    public static final String SIGN_IN = BuildConfig.TASK_URL + "sign/in";

    /**
     * 用户签到翻倍
     */
    public static final String SIGN_DOUBLE = BuildConfig.TASK_URL + "sign/double";

    /**
     * 客户端更新
     */
    public static final String APK_INFO = BuildConfig.TASK_URL + "apk/info";


    public static final String GRADE_WITHDRAW_LIST = BuildConfig.HTTP_AWARD + "wall/v2/deposit/list";

    public static final String WITHDRAW = BuildConfig.HTTP_AWARD + "wall/v2/pay";
    public static final String WITHDRAW_RECORD = BuildConfig.HTTP_AWARD + "wall/v2/deposit/detail";

}
