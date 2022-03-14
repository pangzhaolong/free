package com.donews.middle.api;


import com.donews.middle.BuildConfig;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2021/10/20 15:05<br>
 * 版本：V1.0<br>
 */
public class MiddleApi {

    public final static String lotteryCategoryUrl = BuildConfig.API_LOTTERY_URL + "v1/categories";
    public final static String lotteryGoodsUrl = BuildConfig.API_LOTTERY_URL + "v1/goods-list";
    public final static String walletRedPacketUrl = BuildConfig.API_WALLET_URL + "v1/red-packet";

    public final static String awardListUrl = BuildConfig.API_LOTTERY_URL + "v1/rotation-lottery-info";

    public final static String commandUrl = BuildConfig.BASE_CONFIG_URL + "ddyb-cmdConfig" + BuildConfig.BASE_RULE_URL;

    public final static String frontConfigUrl = BuildConfig.BASE_CONFIG_URL + "ddyb-frontEx" + BuildConfig.BASE_RULE_URL;

    public final static String privilegeLinkUrl = BuildConfig.BASE_URL + "v2/get-privilege-link?goods_id=%s&material_id=%s&search_id=%s&src=%d";
}
