package com.dn.sdk.listener.fullscreenvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.count.CountTrackImpl

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/2 13:46
 */
class TrackFullScreenVideoAdInteractionListener(
    private val adRequest: AdRequest,
    val countTrackImpl: CountTrackImpl,
    private val listener: ITTFullScreenVideoAdData.FullScreenVideoAdInteractionListener?
) : ITTFullScreenVideoAdData.FullScreenVideoAdInteractionListener {
    override fun onAdShow() {
        countTrackImpl.onAdShow()
        listener?.onAdShow()
    }

    override fun onAdVideoBarClick() {
        countTrackImpl.onAdClick()
        listener?.onAdVideoBarClick()
    }

    override fun onAdClose() {
        countTrackImpl.onAdClose()
        listener?.onAdClose()
    }

    override fun onVideoComplete() {
        countTrackImpl.onVideoComplete()
        listener?.onVideoComplete()
    }

    override fun onSkippedVideo() {
        listener?.onSkippedVideo()
    }
}