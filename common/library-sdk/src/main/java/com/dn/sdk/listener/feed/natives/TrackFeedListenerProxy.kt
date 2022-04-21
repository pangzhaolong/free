package com.dn.sdk.listener.feed.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl

/**
 * 源生信息流埋点
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:46
 */
class TrackFeedListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdFeedListener?
) : IAdFeedListener {

    //监听器 没有startLoad方法，直接调用
    private val countTrack = CountTrackImpl(adRequest).apply {
        onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }

    override fun onAdExposure() {
        countTrack.onAdShow()
        listener?.onAdExposure()
    }

    override fun onAdClicked() {
        countTrack.onAdClick()
        listener?.onAdClicked()
    }

    override fun onAdError(errorMsg: String?) {
        listener?.onAdError(errorMsg)
    }
}