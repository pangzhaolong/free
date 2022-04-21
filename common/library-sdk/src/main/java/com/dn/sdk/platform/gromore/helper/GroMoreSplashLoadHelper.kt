package com.dn.sdk.platform.gromore.helper

import android.app.Activity
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.GMAdConstant.DOWNLOAD_TYPE_POPUP
import com.bytedance.msdk.api.v2.GMAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.listener.splash.IAdSplashListener
import com.dn.sdk.platform.BaseHelper
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash
import com.dn.sdk.platform.gromore.preloadad.GroMorePreloadSplashAd

/**
 * GroMore 开屏load
 *
 * @author lcl
 * @version v1.0
 * @date 2022/3/17
 */
object GroMoreSplashLoadHelper : BaseHelper() {

    /** 加载和显示广告 */
    fun loadAndShowAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
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

        if (adRequest.mAdContainer == null) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdContainerNull.code,
                    AdCustomError.ParamsAdContainerNull.errorMsg
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

        val ttSplashAd = GMSplashAd(activity, adRequest.mAdId)
        val adSlot = GMAdSlotSplash.Builder()
            .setImageAdSize(1080, 1920) // 单位px
            .setSplashPreLoad(true)//开屏gdt开屏广告预加载
            .setMuted(false) //声音开启
            .setVolume(1f)//admob 声音配置，与setMuted配合使用
            .setTimeOut(5000)//设置超时
            .setSplashButtonType(SPLASH_BUTTON_TYPE_FULL_SCREEN)//合规设置，点击区域设置
            .setDownloadType(DOWNLOAD_TYPE_POPUP)//合规设置，下载弹窗
            .build()

        ttSplashAd.setAdSplashListener(object : GMSplashAdListener {
            override fun onAdClicked() {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                    listener?.onAdExposure()
                }
            }

            override fun onAdShowFail(p0: AdError) {
                runOnUiThread(activity) {
                    listener?.onAdError(p0.code, p0.message)
                }
            }

            override fun onAdSkip() {

            }

            override fun onAdDismiss() {
                runOnUiThread(activity) {
                    listener?.onAdDismiss()
                }
            }
        })
        ttSplashAd.loadAd(adSlot, object : GMSplashAdLoadCallback {
            override fun onSplashAdLoadFail(p0: AdError) {
                runOnUiThread(activity) {
                    listener?.onAdError(p0.code, p0.message)
                }
            }

            override fun onSplashAdLoadSuccess() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                    ttSplashAd.showAd(adRequest.mAdContainer)
                }
            }

            override fun onAdLoadTimeout() {
                listener?.onAdError(
                    AdCustomError.LoadTimeOutError.code,
                    AdCustomError.LoadTimeOutError.errorMsg
                )
            }
        })
    }

    /** 预加载广告 */
    fun preloadAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ): PreloadSplashAd {
        listener?.onAdStartLoad()

        val ttSplashAd = GMSplashAd(activity, adRequest.mAdId)
        val adSlot = GMAdSlotSplash.Builder()
            .setImageAdSize(adRequest.mWidthDp.toInt(), adRequest.mHeightDp.toInt()) // 单位px
            .setSplashPreLoad(true)//开屏gdt开屏广告预加载
            .setMuted(false) //声音开启
            .setVolume(1f)//admob 声音配置，与setMuted配合使用
            .setTimeOut(5000)//设置超时
            .setSplashButtonType(SPLASH_BUTTON_TYPE_FULL_SCREEN)//合规设置，点击区域设置
            .setDownloadType(DOWNLOAD_TYPE_POPUP)//合规设置，下载弹窗
            .build()

        //封装的预加载对象
        val preloadSplashAd = GroMorePreloadSplashAd(ttSplashAd, adRequest.mAdContainer)
        preloadSplashAd.setLoadState(PreloadAdState.Loading)

        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    preloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdIdNullOrBlank.code,
                        AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                    )
                }
            }
            return preloadSplashAd
        }

        if (adRequest.mAdContainer == null) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    preloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdContainerNull.code,
                        AdCustomError.ParamsAdContainerNull.errorMsg
                    )
                }
            }
            return preloadSplashAd
        }

        if (adRequest.mWidthDp == 0f) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    preloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdWidthDpError.code,
                        AdCustomError.ParamsAdWidthDpError.errorMsg
                    )
                }
            }
            return preloadSplashAd
        }

        if (adRequest.mHeightDp == 0f) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    preloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdHeightDpError.code,
                        AdCustomError.ParamsAdHeightDpError.errorMsg
                    )
                }
            }
            return preloadSplashAd
        }

        ttSplashAd.setAdSplashListener(object : GMSplashAdListener {
            override fun onAdClicked() {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdShow() {
                preloadSplashAd.setLoadState(PreloadAdState.Shown)
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdShowFail(p0: AdError) {
                runOnUiThread(activity) {
                    listener?.onAdError(p0.code, p0.message)
                }
            }

            override fun onAdSkip() {
            }

            override fun onAdDismiss() {
                runOnUiThread(activity) {
                    listener?.onAdDismiss()
                }
            }
        })
        //延迟加载
        DelayExecutor.delayExec(150) {
            ttSplashAd.loadAd(adSlot, object : GMSplashAdLoadCallback {
                override fun onSplashAdLoadFail(p0: AdError) {
                    preloadSplashAd.setLoadState(PreloadAdState.Error)
                    runOnUiThread(activity) {
                        listener?.onAdError(p0.code, p0.message)
                    }
                }

                override fun onSplashAdLoadSuccess() {
                    runOnUiThread(activity) {
                        preloadSplashAd.setLoadState(PreloadAdState.Success)
                        listener?.onAdLoad()
                        if (preloadSplashAd.isNeedShow()) {
                            preloadSplashAd.showAd()
                        }
                    }
                }

                override fun onAdLoadTimeout() {
                    runOnUiThread(activity) {
                        listener?.onAdError(
                            AdCustomError.LoadTimeOutError.code,
                            AdCustomError.LoadTimeOutError.errorMsg
                        )
                    }
                }
            })
        }
        return preloadSplashAd
    }
}