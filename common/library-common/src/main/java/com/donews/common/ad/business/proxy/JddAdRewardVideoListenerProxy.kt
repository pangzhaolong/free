package com.donews.common.ad.business.proxy

import android.app.Activity
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleInterstListener
import com.donews.base.base.AppManager
import com.donews.common.ad.business.callback.JddAdConfigManager
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.monitor.RewardVideoCount
import com.donews.common.ad.business.utils.InterstitialUtils

/**
 * 激励视频回调代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 15:21
 */
class JddAdRewardVideoListenerProxy(
    private val activity: Activity,
    private var listener: IAdRewardVideoListener? = null
) : IAdRewardVideoListener {


    override fun onLoad() {
        listener?.onLoad()
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
    }

    override fun onLoadCached() {
        listener?.onLoadCached()
    }

    override fun onRewardAdShow() {
        listener?.onRewardAdShow()
    }

    override fun onRewardBarClick() {
        listener?.onRewardBarClick()
    }

    override fun onRewardedClosed() {
        RewardVideoCount.playRewardVideoSuccess()
        listener?.onRewardedClosed()

//        JddAdConfigManager.addListener {
//            val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
//            if (jddAdConfigBean.playRewardVideoTimes <= RewardVideoCount.todayPlayRewardVideoTimes()) {
//                if (InterstitialUtils.checkOpenAd(jddAdConfigBean)) {
//                    loadAd()
//                } else {
//                    listener?.onRewardedClosed()
//                }
//            } else {
//                listener?.onRewardedClosed()
//            }
//        }
    }

    override fun onRewardVideoComplete() {
        listener?.onRewardVideoComplete()
    }

    override fun onRewardVideoError() {
        listener?.onRewardVideoError()
    }

    override fun onRewardVideoAdShowFail(code: Int, message: String?) {
        listener?.onRewardVideoAdShowFail(code, message)
    }

    override fun onRewardVerify(result: Boolean) {
        listener?.onRewardVerify(result)
    }

    override fun onSkippedRewardVideo() {
        listener?.onSkippedRewardVideo()
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }

    private fun loadAd() {
        val activity = AppManager.getInstance().topActivity
        AdManager.loadInterstitialAd(activity, object : SimpleInterstListener() {

            override fun onError(code: Int, msg: String?) {
                super.onError(code, msg)
                listener?.onRewardedClosed()
            }

            override fun onAdShow() {
                super.onAdShow()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                listener?.onRewardedClosed()
            }
        })
    }
}