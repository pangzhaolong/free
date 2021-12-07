package com.dn.sdk.loader;

/**
 * SDK 类型
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 13:54
 */
enum class SdkType(val code: Int, val msg: String) {

    /** 关闭广告 */
    CLOSE_AD(0, "广告被关闭"),

    /**多牛聚合*/
    DO_NEWS(1, "doNews"),

    /**多牛封装的穿山甲GroMore平台*/
    DO_GRO_MORE(2, "doNewsGroMore"),

    /** 原生GroMore广告 */
    GRO_MORE(3, "groMore")
}
