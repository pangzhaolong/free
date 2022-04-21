package com.dn.sdk.listener.draw.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTDrawFeedAdData
import com.dn.sdk.count.CountTrackImpl

/**
 * Draw信息流加载埋点
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/2 11:28
 */
class TrackDrawNativeLoadListenerProxy(
    private val adRequest: AdRequest,
    private val listenerNative: IAdDrawNativeLoadListener?
) : IAdDrawNativeLoadListener {

    val countTrack = CountTrackImpl(adRequest)

    override fun onAdError(code: Int, errorMsg: String?) {
        listenerNative?.onAdError(code, errorMsg)
    }

    override fun onAdLoad(list: List<ITTDrawFeedAdData>) {
        listenerNative?.onAdLoad(list)
    }

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listenerNative?.onAdStartLoad()
    }
}