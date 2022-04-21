package com.dn.sdk.count

/**
 *
 * 广告事件追踪埋点事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 15:22
 */
interface ITrack {

    /**
     * 广告开始加载
     */
    fun onAdStartLoad()

    /**
     * 统计广告点击
     */
    fun onAdClick()

    /**
     * 统计广告展示
     */
    fun onAdShow()

    /**
     * 统计广告关闭
     */
    fun onAdClose()


    /**
     * @param verify 是否给予奖励
     */
    fun onRewardVerify(verify: Boolean)


    /**
     * 视频播放完成
     */
    fun onVideoComplete()

}