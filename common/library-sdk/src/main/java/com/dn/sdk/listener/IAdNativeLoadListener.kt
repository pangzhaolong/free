package com.dn.sdk.listener

import com.dn.sdk.bean.natives.INativeAdData

/**
 * 信息流自渲染广告回调监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 13:55
 */
interface IAdNativeLoadListener {

    /** 无广告填充 */
    fun onAdError(code: Int, errorMsg: String?)

    /** 请求广告成功, 返回数据信息*/
    fun onAdLoad(list: List<INativeAdData>)
}