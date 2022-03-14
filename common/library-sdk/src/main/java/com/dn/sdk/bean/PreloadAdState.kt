package com.dn.sdk.bean

/**
 * 预加载广告状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 14:49
 */
enum class PreloadAdState(val state: Int, val msg: String) {

    Init(0, "初始化"),

    Loading(1, "广告加载中"),

    Success(2, "预加载成功"),

    Error(3, "预加载广告失败"),

    Destroy(4, "广告已经被销毁"),

    Shown(5, "广告已经展示过了"),
}