package com.dn.sdk

/**
 * 自定义广告错误
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 16:33
 */
enum class AdCustomError(val code: Int, val errorMsg: String) {

    CloseAd(-10009, "中台已关闭广告"),


    ParamsAdIdNullOrBlank(-10010, "请求参数错误,传入广告id为 null 或者 \"\""),

    ParamsAdContainerNull(-10011, "请求参数错误,传入广告容器为 null"),

    ParamsAdWidthDpError(-10012, "请求参数错误,传入广告宽度为0dp"),

    ParamsAdHeightDpError(-10013, "请求参数错误,传入广告高度为0dp"),

    ParamsAdCountError(-10014, "请求参数错误,请求count 不能超过5"),

    InterstitialIntervalError(-10015, "2次插屏显示间隔时间过短"),

    PreloadTimesError(-10016, "预加载时间过长")
}