package com.donews.front.api;


import com.donews.front.BuildConfig;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2021/10/20 15:05<br>
 * 版本：V1.0<br>
 */
public class FrontApi {

    public final static String lotteryCategoryUrl = BuildConfig.API_LOTTERY_URL + "v1/categories";
    public final static String lotteryGoodsUrl = BuildConfig.API_LOTTERY_URL + "v1/goods-list?category_id=%s&page_size=20&page_id=%d";
    public final static String walletRedPacketUrl = BuildConfig.API_WALLET_URL + "v1/red-packet";

    public final static String walletOpenRedPacketUrl_New = BuildConfig.API_WALLET_URL + "v1/double-red-packet";


    public final static String awardListUrl = BuildConfig.API_LOTTERY_URL + "v1/rotation-lottery-info";

    public final static String winningRotationUrl = BuildConfig.API_LOTTERY_URL + "v1/winning-rotation";

    public final static String lotteryRecordUrl = BuildConfig.API_LOTTERY_URL + "v1/get-today-lottery-period";

    public final static String lotteryDetailUrl = BuildConfig.API_LOTTERY_URL + "v1/detail-open-lottery-record";

    public final static String serverTimeUrl = "https://lottery.xg.tagtic.cn/lottery/v1/get-now-time";

}
