package com.dn.sdk.bean;

/**
 * 广告类型
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 13:58
 */
enum class AdType(val code: Int, val msg: String) {

    /** 开屏广告 */
    SPLASH(0, "splash"),

    /** Banner广告 */
    BANNER(1, "banner"),

    /** 插屏广告 */
    INTERSTITIAL(2, "interstitial"),

    /** 激励视频广告  */
    REWARD_VIDEO(4, "rewardVideo"),

    /**  全屏视频广告*/
    FULL_SCREEN_VIDEO(5, "fullScreenVideo"),

    /** 信息流自渲染 */
    FEED(6, "native"),

    /**信息流模板*/
    FEED_TEMPLATE(7, "nativeTemplate"),

    /**Draw */
    DRAW(8, "draw"),

    /**Draw 模板*/
    DRAW_TEMPLATE(9, "drawTemplate"),

    /**Draw 模板*/
    INTERSTITIAL_FULL(10, "Interstitial_Full")
}
