package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.BuildConfig
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadRewardVideoAd
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DnUnionBean
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
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
        listener?.onAdStartLoad()

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

        val doNewsRewardVideoListener = object : DoNewsAdNative.RewardVideoADListener {

            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    if (code == 10 && any is DnUnionBean) {
                        listener?.onAdStatus(code, AdStatus(any))
                    } else {
                        listener?.onAdStatus(code, any)
                    }
                }
                reportEcpm(code, any)
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
        listener?.onAdStartLoad()

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
                reportEcpm(code, any)
            }

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                }
            }

            override fun onVideoCached() {
                runOnUiThread(activity) {
                    preloadRewardVideoAd.setLoadState(PreloadAdState.Success)
                    listener?.onVideoCached()
                    if (preloadRewardVideoAd.isNeedShow()) {
                        preloadRewardVideoAd.showAd()
                    }
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    preloadRewardVideoAd.setLoadState(PreloadAdState.Shown)
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


    /** 给客户端 上报ecpm */
    private fun reportEcpm(code: Int, any: Any?) {
        if (code == 10 && any is DnUnionBean) {
            if (any.platFormType == "2" || any.platFormType == "3") {
                val ecpm = any.currentEcpm
                val params = JSONObject()
                params.put("req_id", any.reqId)
                params.put("ecpm", ecpm)
                //广告类型,0 缺省,1激励视频
                params.put("type", 1)

                val jsonString = params.toString()

                AdLoggerUtils.d("开始Ecpm上报:$jsonString")
                val url = if (BuildConfig.HTTP_DEBUG) {
                    "http://ecpm-customer.dev.tagtic.cn/api/v1/ecpm/report"
                } else {
                    "http://ecpm-customer.xg.tagtic.cn/api/v1/ecpm/report"
                }
                EasyHttp.post(url)
                    .upJson(jsonString)
                    .cacheMode(CacheMode.NO_CACHE)
                    .execute(object : SimpleCallBack<String>() {
                        override fun onError(e: ApiException?) {
                            AdLoggerUtils.d("上报ecpm 错误:$e")
                        }

                        override fun onSuccess(t: String?) {
                            AdLoggerUtils.d("上报ecpm 成功:$t")
                        }
                    })

            } else {
                AdLoggerUtils.d("当前广告platFormType: ${any.platFormType}无法进行ecpm进行上报")
            }
        } else {
            AdLoggerUtils.d("当前广告code: ${code}无法进行ecpm进行上报")
        }
    }

}