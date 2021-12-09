package com.dn.sdk.bean

import android.view.ViewGroup
import com.dn.sdk.platform.IPlatform
import com.dn.sdk.platform.closead.NoAdPlatform

/**
 * 广告请求对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 14:12
 */
class AdRequest(var mAdType: AdType) {

    /** 请求使用的广告平台 */
    var mPlatform: IPlatform = NoAdPlatform()

    /** 广告id */
    var mAdId: String = ""

    /** 广告id对应的key */
    var mAdKey: String = ""

    /** 请求广告的宽度,单位dp */
    var mWidthDp: Float = 0f

    /** 请求广告的高度,单位dp */
    var mHeightDp: Float = 0f

    /** 广告展示容器 */
    var mAdContainer: ViewGroup? = null

    /** 广告请求超时，默认5秒钟 */
    var mAdRequestTimeOut: Int = 5000

    /** 是否是预加载广告，默认false */
    var mAdPreload: Boolean = false

    /** 请求信息流等数据 数量，默认1，最大5 */
    var mAdCount: Int = 1;

    /** 广告方向，1竖屏，2横屏，默认竖屏 */
    var mOrientation = 1

    /** 激励视频奖励数量，选填，默认1 */
    var mRewardAmount: Int = 1

    /** a激励视频奖励名称，选填，默认金币 */
    var mRewardName: String = "金币"

    /** 现阶段，激励视频选填用户id */
    var mUserId: String = ""
}