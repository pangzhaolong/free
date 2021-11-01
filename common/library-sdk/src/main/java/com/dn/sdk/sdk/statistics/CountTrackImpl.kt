package com.dn.sdk.sdk.statistics

import com.dn.sdk.sdk.AdSdkManager
import com.dn.sdk.sdk.bean.AdType
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.utils.SdkLogUtils
import com.donews.utilslibrary.analysis.AnalysisHelp
import com.donews.utilslibrary.utils.MD5Util

/**
 * 大数据上报事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 15:25
 */
class CountTrackImpl(private val requestInfo: RequestInfo) : ITrack {

    private val adType: AdType = requestInfo.adType
    private val sdkType: SDKType = requestInfo.platform.getLoader().sdkType
    private val requestId: String

    init {
        val key: String = AdSdkManager.getContext().packageName + System.currentTimeMillis()
        requestId = MD5Util.getFileMD5(key.toByteArray())
        event(if (requestInfo.isPreload) AnalysisEvent.AD_LOADING else AnalysisEvent.AD_ACTIVITY)
    }

    override fun onClick() {
        event(AnalysisEvent.AD_CLICK)
    }

    override fun onShow() {
        event(AnalysisEvent.AD_SHOW)
    }

    override fun onAdClose() {
        event(AnalysisEvent.AD_CLOSE)
    }

    override fun onLoadError() {
        event(AnalysisEvent.AD_LOAD_ERROR)
    }

    override fun onADExposure() {
        event(AnalysisEvent.AD_EXPOSURE)
    }

    override fun onRewardVerify(verify: Boolean) {
        event(AnalysisEvent.AD_REWARD_VERIFY)
    }

    override fun onVideoComplete() {
        event(AnalysisEvent.AD_COMPLETE)
    }

    override fun downloadFinished() {
        event(AnalysisEvent.AD_DOWNLOAD_FINISHED)
    }

    private fun event(eventName: String) {
        SdkLogUtils.i(
            SdkLogUtils.TAG, "EventName: " + eventName + " sdk: " + sdkType.description + " adType: "
                    + adType.description + " adId: " + requestInfo.adId
        )
        AnalysisHelp.onEvent(
            AdSdkManager.getContext(),
            eventName,
            sdkType.description,
            adType.description,
            requestInfo.adId,
            requestId
        )
    }
}