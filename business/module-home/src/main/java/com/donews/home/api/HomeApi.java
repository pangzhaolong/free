package com.donews.home.api;

import com.donews.home.BuildConfig;

;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 15:05<br>
 * 版本：V1.0<br>
 */
public class HomeApi {
    private final static String HOME_CONFIG = BuildConfig.BASE_CONFIG_URL;
    private final static String HOME_RULE = BuildConfig.BASE_RULE_URL;

    private final static String HOME_GAME_URL = BuildConfig.HTTP_DEBUG ?
            "https://quiz-game-be.dev.xg.tagtic.cn/recharge-app/" : "https://quiz-game-be.xg.tagtic.cn/recharge-app/";

    /*
     * 精选页面顶部Banner，金刚区展示数据
     * */

    public final static String TopBannerUrl = BuildConfig.BASE_URL + "v1/top-layout";
    public final static String SuperCategory = BuildConfig.BASE_URL + "v1/super-category-list";
    public final static String goodsList = BuildConfig.BASE_URL + "v1/goods-list";
    public final static String searchSugUrl = BuildConfig.BASE_URL + "v1/search-suggestion";
    public final static String searchResultUrl = BuildConfig.BASE_URL + "v1/list-super-goods";
    public final static String realTimeUrl = BuildConfig.BASE_URL + "v1/ranking-list";//https://lottery.dev.tagtic.cn/shop/v1/ranking-list?rank_type=1&page_size=2
    public final static String seckiltUrl = BuildConfig.BASE_URL + "v1/ddq-goods-list";//https://lottery.dev.tagtic.cn/shop/v1/ddq-goods-list?round_time=2021-10-18%2017%3A00%3A00&page_size=2
//    https://lottery.dev.tagtic.cn/user/v1/user-list?limit=3
    public final static String userRandomUrl = BuildConfig.API_USER_URL + "v1/user-list";

    public final static String crazyListUrl = BuildConfig.BASE_URL + "v1/ranking-list";


    public final static String perfectGoodsListUrl = BuildConfig.BASE_URL + "v2/recommend-goods-list";

//    https://lottery.dev.tagtic.cn/shop/v1/ranking-list?rank_type=1&page_size=20&page_id=1
}
