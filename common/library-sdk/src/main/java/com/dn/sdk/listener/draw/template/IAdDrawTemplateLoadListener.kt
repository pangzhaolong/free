package com.dn.sdk.listener.draw.template

import com.dn.sdk.bean.natives.ITTNativeExpressAdData
import com.dn.sdk.listener.IAdStartLoadListener

/**
 * 原生信息流加载监听器
 *
 *  make in st
 *  on 2021/12/27 18:14
 */
interface IAdDrawTemplateLoadListener : IAdStartLoadListener {

    /** 无广告填充 */
    fun onAdError(code: Int, errorMsg: String?)

    /** 请求广告成功, 返回数据信息*/
    fun onAdLoad(list: List<ITTNativeExpressAdData>)
}