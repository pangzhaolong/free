package com.dn.sdk.sdk.statistics

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
     * 统计广告点击
     */
    fun onClick()

    /**
     * 统计广告展示
     */
    fun onShow()

    /**
     * 统计广告关闭
     */
    fun onAdClose()

    /**
     * 统计广告加载失败
     */
    fun onLoadError()

    /**
     * 统计广告曝光成功
     */
    fun onADExposure()

    /**
     * @param verify 是否给予奖励
     */
    fun onRewardVerify(verify: Boolean)


    /**
     * 视频播放完成
     */
    fun onVideoComplete()


    /**
     * app下载完成
     */
    fun downloadFinished()
}