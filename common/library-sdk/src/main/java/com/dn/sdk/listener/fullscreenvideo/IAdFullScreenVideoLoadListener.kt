package com.dn.sdk.listener.fullscreenvideo

import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.listener.IAdStartLoadListener

/**
 * 全屏视屏加载监听器
 *
 *  make in st
 *  on 2021/12/27 18:14
 */
interface IAdFullScreenVideoLoadListener : IAdStartLoadListener {

    /** 无广告填充 */
    fun onAdError(code: Int, errorMsg: String?)

    /** 请求广告成功, 返回数据信息*/
    fun onAdLoad(ad: ITTFullScreenVideoAdData)
}