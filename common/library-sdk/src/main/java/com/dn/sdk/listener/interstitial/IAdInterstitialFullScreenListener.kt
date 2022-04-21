package com.dn.sdk.listener.interstitial

import com.dn.sdk.listener.IAdStartLoadListener


/**
 * 插屏的全屏广告(插全屏广告)
 *
 * @author lcl
 * @version v1.0
 * @date 2021/9/26 14:59
 */
interface IAdInterstitialFullScreenListener : IAdStartLoadListener {


    /**
     *
     * code为10 且object instanceof DnUnionBean 时才可获取对应的参数,具体参数如下:
     * String reqId   串联每次广告流程的reqid,每次请求都是不同的值。
     * String appId   app自己的appId
     * String positionId 多牛聚合后台的聚合广告位Id
     * String groMorePostionId 穿山甲聚合广告位id 只有在platFormType位2 和3时候才会有值
     * String currentPostionId 当前展示广告的联盟广告位ID
     * private String platFormType 平台类型 1多牛 2 gromore 3 optGromroe 4 opt
     * private String currentEcpm 当前展示广告的ecpm，只有platFormType为2和3才可能会有效值，此值返回有特殊条件。
     * @param code Int 返回code
     * @param any Any 返回对象
     */
    fun onAdStatus(code: Int, any: Any?)

    //数据在线加载成功
    fun onAdLoad()

    //数据本地缓存成功，强烈建议在收到此接口回调之后调用show方法
    fun onAdCached()

    //广告加载失败
    fun onAdError(errorCode: Int, errprMsg: String)

    //广告展示以及曝光
    fun onAdShow()

    //广告被点击
    fun onAdClicked()

    //广告播放完成 此接口不一定给回调
    fun onAdComplete()

    //广告关闭 必回调 下一次视频缓存再此接口之后进行
    fun onAdClose()

    //调用视频接口，此接口不一定给回调
    fun onSkippedVideo()

    //广告可以被激励,true为可以发奖励，false不能发奖励，此接口不一定给回调，没有激励类型
    //的广告是没有回调的。
    fun onRewardVerify(reward: Boolean)

    //广告展示失败
    fun onAdShowFail(errCode: Int, errMsg: String)

    //广告视频播放出现错误
    fun onAdVideoError(errCode: Int, errMsg: String)
}
