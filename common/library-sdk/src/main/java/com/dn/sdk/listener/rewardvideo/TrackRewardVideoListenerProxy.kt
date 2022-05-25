package com.dn.sdk.listener.rewardvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.count.CountTrackImpl

/**
 * 激励视频埋点事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:52
 */
class TrackRewardVideoListenerProxy(
        var adRequest: AdRequest,
        var listener: IAdRewardVideoListener?
) : IAdRewardVideoListener {

    private val countTrack = CountTrackImpl(adRequest)
    private var mAdActionBean: AdActionBean = AdActionBean()

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        if (code == 10 && any is AdStatus) {
            mAdActionBean.ad_req_id = any.reqId
            mAdActionBean.platform = any.platFormType?.let { it.toInt() }
            mAdActionBean.event_type = AdEventType.AdEventVideoStart
            mAdActionBean.ecpm = any.currentEcpm.toDouble().toInt()
            mAdActionBean.platform = any.platFormType.toInt()
            mAdActionBean.union_app_id = any.appId
            mAdActionBean.position_id = any.currentPositionId
            mAdActionBean.union_position_id = any.positionId
            mAdActionBean.place_attribute = AdPlaceAttribute.AdPlaceAttributeRewardedVideo
            mAdActionBean.event_type = AdEventType.AdEventVideoStart
            YfDcHelper.onAdActionEvent(mAdActionBean)
        }

        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        mAdActionBean.event_type = AdEventType.AdEventImpress
        YfDcHelper.onAdActionEvent(mAdActionBean)
        countTrack.onAdShow()
        listener?.onAdShow()
    }

    override fun onAdVideoClick() {
        mAdActionBean.event_type = AdEventType.AdEventClick
        YfDcHelper.onAdActionEvent(mAdActionBean)
        countTrack.onAdClick()
        listener?.onAdVideoClick()
    }

    override fun onAdSkipped() {
        listener?.onAdSkipped()
    }

    override fun onRewardVerify(result: Boolean) {
        if (result) {
            mAdActionBean.event_type = AdEventType.AdEventVideoConduct
            YfDcHelper.onAdActionEvent(mAdActionBean)
        }
        countTrack.onRewardVerify(result)
        listener?.onRewardVerify(result)
    }

    override fun onAdClose() {
        mAdActionBean.event_type = AdEventType.AdEventVideoEnd
        YfDcHelper.onAdActionEvent(mAdActionBean)
        countTrack.onAdClose()
        listener?.onAdClose()
    }

    override fun onVideoCached() {
        listener?.onVideoCached()
    }

    override fun onVideoComplete() {
        countTrack.onVideoComplete()
        listener?.onVideoComplete()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }

    override fun addReportEcpmParamsWhenReward(params: EcpmParam) {
        listener?.addReportEcpmParamsWhenReward(params)
    }

    override fun reportEcpmSuccessWhenReward(response: EcpmResponse) {
        listener?.reportEcpmSuccessWhenReward(response)
    }

    override fun reportEcpmFailWhenReward() {
        listener?.reportEcpmFailWhenReward()
    }
}