package com.dn.sdk.bean.natives

import android.app.Activity
import android.view.View
import androidx.annotation.MainThread
import com.bytedance.sdk.openadsdk.DislikeInfo
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAdDislike.DislikeInteractionCallback
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTDislikeDialogAbstract

/**
 *  make in st
 *  on 2021/12/28 09:38
 */
interface ITTNativeExpressAdData {

    fun getExpressAdView(): View?

    fun getImageMode(): Int

    fun getDislikeInfo(): DislikeInfo?

    fun setExpressInteractionListener(var1: ExpressAdInteractionListener?)

    fun setExpressInteractionListener(var1: AdInteractionListener?)

    fun setDownloadListener(var1: TTAppDownloadListener?)

    fun getInteractionType(): Int

    fun render()

    fun destroy()

    fun setDislikeCallback(var1: Activity?, var2: DislikeInteractionCallback?)

    fun setDislikeDialog(var1: TTDislikeDialogAbstract?)

    fun getDislikeDialog(var1: Activity?): TTAdDislike?

    @MainThread
    fun showInteractionExpressAd(var1: Activity?)

    fun setSlideIntervalTime(var1: Int)

    fun setVideoAdListener(var1: ExpressVideoAdListener?)

    fun setCanInterruptVideoPlay(var1: Boolean)

    fun getMediaExtraInfo(): Map<String?, Any?>?

    interface ExpressVideoAdListener {
        fun onVideoLoad()

        fun onVideoError(errorCode: Int, extraCode: Int)

        fun onVideoAdStartPlay()

        fun onVideoAdPaused()

        fun onVideoAdContinuePlay()

        fun onProgressUpdate(current: Long, duration: Long)

        fun onVideoAdComplete()

        fun onClickRetry()
    }

    interface AdInteractionListener : ExpressAdInteractionListener {
        fun onAdDismiss()
    }

    interface ExpressAdInteractionListener {
        fun onAdClicked(view: View?, type: Int)

        fun onAdShow(view: View?, type: Int)

        fun onRenderFail(view: View?, msg: String?, code: Int)

        fun onRenderSuccess(view: View?, width: Float, height: Float)
    }

}