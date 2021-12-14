package com.donews.common.ad.business.proxy

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.listener.impl.SimpleInterstitialListener
import com.dn.sdk.manager.config.IAdConfigInitListener
import com.dn.sdk.platform.donews.natives.DoNewsNativeData
import com.donews.base.base.AppManager
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.donews.common.ad.business.manager.JddAdManager
import com.donews.common.ad.business.monitor.RewardVideoCount
import com.donews.common.ad.business.utils.InterstitialUtils
import com.donews.common.base.MvvmBaseLiveDataActivity

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

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }

    override fun onAdStartLoad() {
        listener?.onAdStartLoad()
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        listener?.onAdShow()
        RewardVideoCount.playRewardVideoSuccess()
    }

    override fun onAdVideoClick() {
        listener?.onAdVideoClick()
        RewardVideoCount.rewardVideoClick()
    }

    override fun onRewardVerify(result: Boolean) {
        listener?.onRewardVerify(result)
    }

    override fun onAdClose() {
        JddAdManager.addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
                if (jddAdConfigBean.playRewardVideoTimes <= RewardVideoCount.todayPlayRewardVideoTimes()) {
                    if (InterstitialUtils.checkOpenAd(jddAdConfigBean)) {
                        loadAd()
                    } else {
                        listener?.onAdClose()
                    }
                } else {
                    listener?.onAdClose()
                }
            }
        })
    }

    override fun onVideoCached() {
        listener?.onVideoCached()
    }

    override fun onVideoComplete() {
        listener?.onVideoComplete()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }


    private fun loadAd() {
        var resultActivity = AppManager.getInstance().topActivity
        if (resultActivity !is MvvmBaseLiveDataActivity<*, *>) {
            resultActivity = AppManager.getInstance().secondActivity
            if (resultActivity !is MvvmBaseLiveDataActivity<*, *>) {
                listener?.onAdClose()
                return
            }
        }
        (resultActivity).showLoading("加载中...")
        AdManager.loadInterstitialAd(resultActivity, object : SimpleInterstitialListener() {

            override fun onAdShow() {
                super.onAdShow()
                resultActivity.hideLoading()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                resultActivity.hideLoading()
                listener?.onAdClose()
            }


            override fun onAdClosed() {
                super.onAdClosed()
                listener?.onAdClose()
            }
        })
    }
}