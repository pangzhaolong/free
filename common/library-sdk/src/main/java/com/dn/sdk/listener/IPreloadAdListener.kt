package com.dn.sdk.listener

import com.dn.sdk.bean.preload.PreloadAd

/**
 * 预加载广告返回类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 16:46
 */
interface IPreloadAdListener {

    fun onLoadAd(ad: PreloadAd)
}