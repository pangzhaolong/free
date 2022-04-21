package com.dn.sdk.listener.feed.template

import android.view.View
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl

/**
 * 模板信息流 埋点事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:49
 */
class TrackFeedTemplateListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdFeedTemplateListener?
) : IAdFeedTemplateListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad(views: MutableList<View>) {
        listener?.onAdLoad(views)
    }

    override fun onAdShow() {
        listener?.onAdShow()
    }

    override fun onAdExposure() {
        countTrack.onAdShow()
        listener?.onAdExposure()
    }

    override fun onAdClicked() {
        countTrack.onAdClick()
        listener?.onAdClicked()
    }

    override fun onAdClose() {
        countTrack.onAdClose()
        listener?.onAdClose()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }
}