package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadInterstitialAd
import com.dn.sdk.platform.gromore.preloadad.GroMorePreloadInterstitialAd
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DnUnionBean
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD

/**
 * 多牛v2 加载插屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 16:01
 */
object DoNewsInterstitialLoadHelper : BaseHelper() {

    /**
     *
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdInterstitialListener?
     */
    fun preloadInterstitialAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ): PreloadInterstitialAd {
        runOnUiThread(activity) {
            listener?.onAdStartLoad()
        }
        val doNewsAD = DoNewsAD.Builder()
            .setPositionId(adRequest.mAdId)
            .setExpressViewWidth(adRequest.mWidthDp) //宽单位dp 必传，不能为0，
            .setExpressViewHeight(adRequest.mHeightDp) //高dp 必传，不能为0
            .build()
        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        val preloadAd = DoNewsPreloadInterstitialAd(doNewsAdNative, activity)
        preloadAd.setLoadState(PreloadAdState.Loading)
        DelayExecutor.delayExec(100) {
            doNewsAdNative.loadInterstitial(activity, doNewsAD,
                object : DoNewsAdNative.DonewsInterstitialADListener {
                    override fun onAdStatus(code: Int, any: Any?) {
                        runOnUiThread(activity) {
                            if (code == 10 && any is DnUnionBean) {
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

                    override fun onAdShow() {
                        runOnUiThread(activity) {
                            listener?.onAdShow()
                        }
                    }

                    override fun onAdExposure() {
                        runOnUiThread(activity) {
                            listener?.onAdExposure()
                        }
                    }

                    override fun onAdClicked() {
                        runOnUiThread(activity) {
                            listener?.onAdClicked()
                        }
                    }

                    override fun onAdClosed() {
                        runOnUiThread(activity) {
                            listener?.onAdClosed()
                        }
                    }

                    override fun onAdError(code: Int, errorMsg: String?) {
                        runOnUiThread(activity) {
                            listener?.onAdError(code, errorMsg)
                        }
                    }
                })
        }
        return preloadAd
    }

    /** 加载和显示广告，插屏广告必须传递广告id，和广告宽高属性 */
    fun loadAndShowAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ) {
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

        if (adRequest.mWidthDp == 0f) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdWidthDpError.code,
                    AdCustomError.ParamsAdWidthDpError.errorMsg
                )
            }
            return
        }

        if (adRequest.mHeightDp == 0f) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdHeightDpError.code,
                    AdCustomError.ParamsAdHeightDpError.errorMsg
                )
            }
            return
        }

        val doNewsNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        bindLifecycle(activity, doNewsNative)

        val doNewsInterstitialListener = object : DoNewsAdNative.DonewsInterstitialADListener {
            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    if (code == 10 && any is DnUnionBean) {
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

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdExposure() {
                runOnUiThread(activity) {
                    listener?.onAdExposure()
                }
            }

            override fun onAdClicked() {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdClosed() {
                runOnUiThread(activity) {
                    listener?.onAdClosed()
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            .setPositionId(adRequest.mAdId)
            .setExpressViewWidth(adRequest.mWidthDp)
            .setExpressViewHeight(adRequest.mHeightDp)
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        doNewsNative.loadAndShowInterstitial(activity, doNewsAd, doNewsInterstitialListener)
    }
}