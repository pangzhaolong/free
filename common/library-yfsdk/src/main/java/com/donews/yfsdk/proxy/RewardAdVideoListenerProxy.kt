package com.donews.yfsdk.proxy

import android.app.Activity
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.donews.yfsdk.check.RewardVideoCheck

/**
 * 激励视频回调代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 15:21
 */
class RewardAdVideoListenerProxy(
        var activity: Activity?,
        var listener: IAdRewardVideoListener? = null,
        var needReportEcpmWhenReward: Boolean = false
) : IAdRewardVideoListener {

    private var mAdStatus: AdStatus? = null

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
        if (code == 10 && any is AdStatus) {
            mAdStatus = any
        }

        RewardVideoCheck.reportEcpm(code, any)
    }

    override fun onAdStartLoad() {
        listener?.onAdStartLoad()
        RewardVideoCheck.loadAdStart()
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
        RewardVideoCheck.onAdLoad()
    }

    override fun onAdShow() {
        listener?.onAdShow()
        RewardVideoCheck.onAdShow()
    }

    override fun onAdVideoClick() {
        listener?.onAdVideoClick()
        RewardVideoCheck.onAdVideoClick(mAdStatus)
    }

    override fun onAdSkipped() {
        listener?.onAdSkipped()
    }

    override fun onRewardVerify(result: Boolean) {
        listener?.onRewardVerify(result)
        if (result && needReportEcpmWhenReward) {
            RewardVideoCheck.reportEcpmWhenReward(activity, listener)
        }
    }

    override fun onAdClose() {
        listener?.onAdClose()
        RewardVideoCheck.loadAdClose()
    }

    override fun onVideoCached() {
        listener?.onVideoCached()
    }

    override fun onVideoComplete() {
        listener?.onVideoComplete()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
        RewardVideoCheck.loadRewardVideoFail()
        if (code == 3000) {
            RewardVideoCheck.loadAdNoFill()
        }
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