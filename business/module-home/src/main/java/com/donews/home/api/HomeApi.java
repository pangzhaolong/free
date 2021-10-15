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
    /**
     * 首页游戏的配置
     */
    public final static String HOME_RECHARGE = HOME_CONFIG + "homeRecharge" + HOME_RULE;

    /**
     * 登陆接口: (需要初始化一些内容)
     */
    public final static String HOME_LOGIN = HOME_GAME_URL + "login";
    /**
     * 首页信息:
     */
    public final static String HOME_INFO = HOME_GAME_URL + "info";
    /**
     * 充电接口:
     */
    public final static String HOME_INDEX_RECHARGE = HOME_GAME_URL + "recharge";
    /**
     * 获取当前奖励数:
     */
    public final static String HOME_GET_REWARD = HOME_GAME_URL + "getReward";

    /**
     * 领取金币接口:
     */
    public final static String HOME_GET_RECEIVE = HOME_GAME_URL + "receive";

    /**
     * 游戏滑动页面
     */
    public final static String HOME_WEB_URL = BuildConfig.HTTP_GAME_URL;

    /**
     * 每日任务页面
     */
    public final static String HOME_WEB_DAILY_TASK = BuildConfig.HTTP_H5 + "homeIntegral";

    /**
     * 幸运金币
     */

    public final static String LUCK_GOLD = BuildConfig.HTTP_LUCK_GOLD + "get_luck";

    /**
     * 领取金币
     */

    public final static String ADD_GOLD = BuildConfig.HTTP_LUCK_GOLD + "add_luck";

    /**
     * 签到
     */

    public final static String SIGN = BuildConfig.HTTP_AWARD + "award/v1/sign/list";


    /*
     * 精选页面顶部Banner，金刚区展示数据
     * */

    public final static String BaseUrl = BuildConfig.BASE_URL;
    public final static String TopBannerUrl = BaseUrl + "/v1/top-layout";
    public final static String SuperCategory = BaseUrl + "/v1/super-category-list";
    public final static String goodsList = BaseUrl + "/v1/goods-list";
    public final static String searchSugUrl = BaseUrl + "/v1/search-suggestion";
    public final static String searchResultUrl = BaseUrl + "/v1/list-super-goods";

}
