package com.dn.sdk

/**
 * 自定义广告错误
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 16:33
 */
enum class AdCustomError(val code: Int, val errorMsg: String) {

    ContextError(-10007, "Activity或者Context 为null"),

    LimitAdError(-10008, "暂无新视频，请稍后再试"),

    CloseAd(-10009, "中台已关闭广告"),


    ParamsAdIdNullOrBlank(-10010, "请求参数错误,传入广告id为 null 或者 \"\""),

    ParamsAdContainerNull(-10011, "请求参数错误,传入广告容器为 null"),

    ParamsAdWidthDpError(-10012, "请求参数错误,传入广告宽度为0dp"),

    ParamsAdHeightDpError(-10013, "请求参数错误,传入广告高度为0dp"),

    ParamsAdCountError(-10014, "请求参数错误,请求count 不能超过5"),

    InterstitialUnknownError(-10020, "插屏未知错误"),

    InterstitialOpenError(-10021, "用户安装时间小于设置的插屏开启时间"),

    InterstitialIntervalError(-10022, "2次插屏显示间隔时间过短"),

    InterstitialHadShowError(-10023, "当前已经展示了一个插屏广告"),

    PreloadTimesError(-10030, "预加载时间过长"),

    /** 本地缓存及无效广告都加载失败的情况 */
    PreloadAdEmptyError(-10031, "暂无新视频，请稍后再试"),

    PreloadAdStatusError(-10032, "预加载广告状态错误")
}