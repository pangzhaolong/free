package com.dn.sdk.listener


/**
 * 插屏广告监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:59
 */
interface IAdInterstitialListener:IAdStartLoadListener {


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

    /** 数据加载成功 */
    fun onAdLoad()

    /** 广告开始展示 */
    fun onAdShow()

    /** 广告曝光 */
    fun onAdExposure()

    /** 广告点击 */
    fun onAdClicked()

    /** 广告关闭*/
    fun onAdClosed()

    /** 无广告填充 */
    fun onAdError(code: Int, errorMsg: String?)
}
