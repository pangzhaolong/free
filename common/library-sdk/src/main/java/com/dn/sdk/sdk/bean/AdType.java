package com.dn.sdk.sdk.bean;

/**
 * 广告类型
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 13:58
 */
public enum AdType {

    /**
     * 开屏
     */
    SPLASH(0, "splash"),

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
    REWARD_VIDEO(4, "rewardVideo"),

    /**
     * 全屏视频
     */
    FULL_SCREEN_VIDEO(5, "fullScreenVideo"),

    /**
     * 信息流自渲染模式
     */
    NEWS_FEED_CUSTOM_RENDER(6, "feedCustomRender"),

    /**
     * 信息流模板
     */
    NEWS_FEED_TEMPLATE(7, "feedTemplate"),

    /**
     * Draw
     */
    DRAW(8, "draw");

    public int value;
    public String description;

    AdType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
