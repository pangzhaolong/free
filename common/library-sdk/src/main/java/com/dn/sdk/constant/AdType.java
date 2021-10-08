package com.dn.sdk.constant;

/**
 * @author by SnowDragon
 * Date on 2020/11/19
 * Description:
 */


public enum AdType {

    /**
     * 开屏
     */
    SPLASH(0, "spread"),


    /**
     * Banner
     */
    BANNER(1, "banner"),

    /**
     * 插屏
     */
    INTERSTITIAL(2, "interstitial"),
    /**
     * 激励视频
     */
    REWARD_VIDEO(4, "video"),
    /**
     * 全屏视频
     */
    FULL_SCREEN_VIDEO(5, "fullVideo"),

    /**
     * 信息流自渲染模式
     */
    NEWS_FEED_CUSTOM_RENDER(6, "self"),

    /**
     * 信息流模板
     */
    NEWS_FEED_TEMPLATE(7, "temp"),

    /**
     * Draw
     */
    DRAW(8, "draw");


    public int VALUE;
    public String DESCRIPTION;

    AdType(int i, String description) {
        VALUE = i;
        DESCRIPTION = description;
    }
}
