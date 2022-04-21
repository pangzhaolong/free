package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.BuildConfig
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.*
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadRewardVideoAd
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DnUnionBean
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.CallBack
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.sdk.getSuuid
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils
import org.json.JSONObject

/**
 * 多牛v2 激励视频广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 17:18
 */
object DoNewsRewardVideoLoadHelper : BaseHelper() {

    fun loadAndShowAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?) {
        runOnUiThread(activity) {
            listener?.onAdStartLoad()
        }
        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdIdNullOrBlank.code,
                    AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                )
            }
            return
        }

        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        //生命周期监听
        bindLifecycle(activity, doNewsAdNative)

        var ecpmValue = 0.0f
        val doNewsRewardVideoListener = object : DoNewsAdNative.RewardVideoADListener {

            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    if (code == 10 && any is DnUnionBean) {
                        if (any.platFormType == "2" || any.platFormType == "3") {
                            ecpmValue = any.currentEcpm.toFloat()
                        }
                        listener?.onAdStatus(code, AdStatus(any))
                    } else {
                        listener?.onAdStatus(code, any)
                    }
                }
            }

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                }
            }

            override fun onVideoCached() {
                runOnUiThread(activity) {
                    listener?.onVideoCached()
                    //缓存成功,则播放激励视频
                    doNewsAdNative?.showRewardAd()
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdVideoClick() {
                runOnUiThread(activity) {
                    listener?.onAdVideoClick()
                }
            }

            override fun onRewardVerify(result: Boolean) {
                runOnUiThread(activity) {
                    listener?.onRewardVerify(result)
                }
            }

            override fun onVideoComplete() {
                runOnUiThread(activity) {
                    listener?.onVideoComplete()
                }
            }

            override fun onAdClose() {
                runOnUiThread(activity) {
                    listener?.onAdClose()
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            //广告位id
            .setPositionId(adRequest.mAdId)
            //视频方向
            .setOrientation(adRequest.mOrientation)
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        doNewsAdNative.loadRewardVideo(activity, doNewsAd, doNewsRewardVideoListener)
    }


    fun preloadAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?): PreloadRewardVideoAd {
        runOnUiThread(activity) {
            listener?.onAdStartLoad()
        }
        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()

        //封装的预加载对象
        val preloadRewardVideoAd = DoNewsPreloadRewardVideoAd(doNewsAdNative)
        preloadRewardVideoAd.setLoadState(PreloadAdState.Loading)

        bindLifecycle(activity, doNewsAdNative) {
            preloadRewardVideoAd.destroy()
        }

        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    preloadRewardVideoAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdIdNullOrBlank.code,
                        AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                    )
                }
            }
            return preloadRewardVideoAd
        }

        val doNewsRewardVideoListener = object : DoNewsAdNative.RewardVideoADListener {

            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    if (code == 10 && any is DnUnionBean) {
                        listener?.onAdStatus(code, AdStatus(any))
                    } else {
                        listener?.onAdStatus(code, any)
                    }
                }
//                reportEcpm(code, any)
            }

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                }
            }

            override fun onVideoCached() {
                runOnUiThread(activity) {
                    listener?.onVideoCached()
                    preloadRewardVideoAd.setLoadState(PreloadAdState.Success)
                    if (preloadRewardVideoAd.isNeedShow()) {
                        preloadRewardVideoAd.showAd()
                    }
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdVideoClick() {
                runOnUiThread(activity) {
                    listener?.onAdVideoClick()
                }
            }

            override fun onRewardVerify(result: Boolean) {
                runOnUiThread(activity) {
                    listener?.onRewardVerify(result)
                }
            }

            override fun onVideoComplete() {
                runOnUiThread(activity) {
                    listener?.onVideoComplete()
                }
            }

            override fun onAdClose() {
                runOnUiThread(activity) {
                    listener?.onAdClose()
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    preloadRewardVideoAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(code, errorMsg)
                }
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            //广告位id
            .setPositionId(adRequest.mAdId)
            //视频方向
            .setOrientation(adRequest.mOrientation)
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        DelayExecutor.delayExec(100) {
            doNewsAdNative.loadRewardVideo(activity, doNewsAd, doNewsRewardVideoListener)
        }
        return preloadRewardVideoAd
    }
}