package com.dn.sdk.listener.rewardvideo;

import com.dn.sdk.listener.IAdEcpmReportListener
import com.dn.sdk.listener.IAdStartLoadListener


/**
 * 激励视频监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:46
 */
interface IAdRewardVideoListener : IAdStartLoadListener, IAdEcpmReportListener {

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

    /** 数据在线加载成功 */
    fun onAdLoad()

    /** 广告展示及曝光 */
    fun onAdShow()

    /** 广告被点击 */
    fun onAdVideoClick()

    /** 广告可以被激励,true为可以发奖励，false不能发奖励，奖励已这个为准 */
    fun onRewardVerify(result: Boolean)

    /** 广告关闭，必定被回调，下一个视频缓存再此回调后调用 */
    fun onAdClose()

    /** 数据本地缓存完成,可以在此接口调用show() */
    fun onVideoCached()

    /** 广告视频播放完成 */
    fun onVideoComplete()

    /** 广告失败 */
    fun onAdError(code: Int, errorMsg: String?)

}
