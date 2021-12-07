package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadRewardVideoAd
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD

/**
 * 多牛v2 激励视频广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 17:18
 */
object DoNewsRewardVideoLoadHelper : BaseHelper() {

    fun loadAndShowAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?) {
        if (adRequest.mAdId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        //生命周期监听
        bindLifecycle(activity, doNewsAdNative)

        val doNewsRewardVideoListener = object : DoNewsAdNative.RewardVideoADListener {

            override fun onAdStatus(code: Int, any: Any?) {
                listener?.onAdStatus(code, any)
            }

            override fun onAdLoad() {
                listener?.onAdLoad()
            }

            override fun onVideoCached() {
                listener?.onVideoCached()
                //缓存成功,则播放激励视频
                doNewsAdNative?.showRewardAd()
            }

            override fun onAdShow() {
                listener?.onAdShow()
            }

            override fun onAdVideoClick() {
                listener?.onAdVideoClick()
            }

            override fun onRewardVerify(result: Boolean) {
                listener?.onRewardVerify(result)
            }

            override fun onVideoComplete() {
                listener?.onVideoComplete()
            }

            override fun onAdClose() {
                listener?.onAdClose()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                listener?.onAdError(code, errorMsg)
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
        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()

        //封装的预加载对象
        val preloadRewardVideoAd = DoNewsPreloadRewardVideoAd(doNewsAdNative)
        preloadRewardVideoAd.setLoadState(PreloadAdState.Loading)

        bindLifecycle(activity, doNewsAdNative) {
            preloadRewardVideoAd.destroy()
        }

        if (adRequest.mAdId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return preloadRewardVideoAd
        }

        val doNewsRewardVideoListener = object : DoNewsAdNative.RewardVideoADListener {

            override fun onAdStatus(code: Int, any: Any?) {
                listener?.onAdStatus(code, any)
            }

            override fun onAdLoad() {
                listener?.onAdLoad()
            }

            override fun onVideoCached() {
                listener?.onVideoCached()
                preloadRewardVideoAd.setLoadState(PreloadAdState.Success)
                if (preloadRewardVideoAd.isNeedShow()) {
                    preloadRewardVideoAd.showAd()
                }
            }

            override fun onAdShow() {
                listener?.onAdShow()
            }

            override fun onAdVideoClick() {
                listener?.onAdVideoClick()
            }

            override fun onRewardVerify(result: Boolean) {
                listener?.onRewardVerify(result)
            }

            override fun onVideoComplete() {
                listener?.onVideoComplete()
            }

            override fun onAdClose() {
                listener?.onAdClose()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                preloadRewardVideoAd.setLoadState(PreloadAdState.Error)
                listener?.onAdError(code, errorMsg)
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
        return preloadRewardVideoAd
    }
}