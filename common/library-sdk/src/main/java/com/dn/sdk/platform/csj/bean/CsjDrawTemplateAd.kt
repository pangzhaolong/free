package com.dn.sdk.platform.csj.bean

import android.app.Activity
import android.view.View
import com.bytedance.msdk.api.nativeAd.TTNativeExpressAdListener
import com.bytedance.sdk.openadsdk.*
import com.dn.sdk.bean.natives.ITTNativeExpressAdData

/**
 * 穿山甲 Draw 模板广告对象
 *
 *  make in st
 *  on 2021/12/28 09:30
 */
class CsjDrawTemplateAd(
    private val nativeData: TTNativeExpressAd
) : ITTNativeExpressAdData {

    override fun getExpressAdView(): View? {
        return nativeData.expressAdView
    }

    override fun getImageMode(): Int {
        return nativeData.imageMode
    }

    override fun getDislikeInfo(): DislikeInfo? {
        return nativeData.dislikeInfo
    }

    override fun setExpressInteractionListener(var1: ITTNativeExpressAdData.ExpressAdInteractionListener?) {
        val listener = object : TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(p0: View?, p1: Int) {
                var1?.onAdClicked(p0, p1)
            }

            override fun onAdShow(p0: View?, p1: Int) {
                var1?.onAdShow(p0, p1)
            }

            override fun onRenderFail(p0: View?, p1: String?, p2: Int) {
                var1?.onRenderFail(p0, p1, p2)
            }

            override fun onRenderSuccess(p0: View?, p1: Float, p2: Float) {
                var1?.onRenderSuccess(p0, p1, p2)
            }

        }
        return nativeData.setExpressInteractionListener(listener)
    }

    override fun setExpressInteractionListener(var1: ITTNativeExpressAdData.AdInteractionListener?) {
        val listener = object : TTNativeExpressAd.AdInteractionListener {
            override fun onAdClicked(p0: View?, p1: Int) {
                var1?.onAdClicked(p0, p1)
            }

            override fun onAdShow(p0: View?, p1: Int) {
                var1?.onAdShow(p0, p1)
            }

            override fun onRenderFail(p0: View?, p1: String?, p2: Int) {
                var1?.onRenderFail(p0, p1, p2)
            }

            override fun onRenderSuccess(p0: View?, p1: Float, p2: Float) {
                var1?.onRenderSuccess(p0, p1, p2)
            }

            override fun onAdDismiss() {
                var1?.onAdDismiss()
            }

        }
        return nativeData.setExpressInteractionListener(listener)
    }

    override fun setDownloadListener(var1: TTAppDownloadListener?) {
        return nativeData.setDownloadListener(var1)
    }

    override fun getInteractionType(): Int {
        return nativeData.interactionType
    }

    override fun render() {
        return nativeData.render()
    }

    override fun destroy() {
        return nativeData.destroy()
    }

    override fun setDislikeCallback(
        var1: Activity?,
        var2: TTAdDislike.DislikeInteractionCallback?
    ) {
        return nativeData.setDislikeCallback(var1, var2)
    }

    override fun setDislikeDialog(var1: TTDislikeDialogAbstract?) {
        return nativeData.setDislikeDialog(var1)
    }

    override fun getDislikeDialog(var1: Activity?): TTAdDislike? {
        return nativeData.getDislikeDialog(var1)
    }

    override fun showInteractionExpressAd(var1: Activity?) {
        return nativeData.showInteractionExpressAd(var1)
    }

    override fun setSlideIntervalTime(var1: Int) {
        return nativeData.setSlideIntervalTime(var1)
    }

    override fun setVideoAdListener(var1: ITTNativeExpressAdData.ExpressVideoAdListener?) {
        val listener = object : TTNativeExpressAd.ExpressVideoAdListener {
            override fun onVideoLoad() {
                var1?.onVideoLoad()
            }

            override fun onVideoError(p0: Int, p1: Int) {
                var1?.onVideoError(p0, p1)
            }

            override fun onVideoAdStartPlay() {
                var1?.onVideoAdStartPlay()
            }

            override fun onVideoAdPaused() {
                var1?.onVideoAdPaused()
            }

            override fun onVideoAdContinuePlay() {
                var1?.onVideoAdContinuePlay()
            }

            override fun onProgressUpdate(p0: Long, p1: Long) {
                var1?.onProgressUpdate(p0, p1)
            }

            override fun onVideoAdComplete() {
                var1?.onVideoAdComplete()
            }

            override fun onClickRetry() {
                var1?.onClickRetry()
            }

        }


        return nativeData.setVideoAdListener(listener)
    }

    override fun setCanInterruptVideoPlay(var1: Boolean) {
        return nativeData.setCanInterruptVideoPlay(var1)
    }

    override fun getMediaExtraInfo(): Map<String?, Any?>? {
        return nativeData.mediaExtraInfo
    }


}