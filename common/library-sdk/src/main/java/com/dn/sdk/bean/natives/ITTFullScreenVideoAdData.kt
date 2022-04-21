package com.dn.sdk.bean.natives

import android.app.Activity
import androidx.annotation.MainThread
import com.bytedance.sdk.openadsdk.TTAdConstant.RitScenes
import com.bytedance.sdk.openadsdk.TTAppDownloadListener

/**
 * 全屏视频对象
 *
 *  make in st
 *  on 2022/1/17 10:49
 */
interface ITTFullScreenVideoAdData {

    fun setFullScreenVideoAdInteractionListener(var1: FullScreenVideoAdInteractionListener?)

    fun setDownloadListener(var1: TTAppDownloadListener?)

    fun getInteractionType(): Int

    @MainThread
    fun showFullScreenVideoAd(var1: Activity?)

    fun showFullScreenVideoAd(var1: Activity?, var2: RitScenes?, var3: String?)

    fun setShowDownLoadBar(var1: Boolean)

    fun getMediaExtraInfo(): Map<String?, Any?>?

    fun getFullVideoAdType(): Int

    fun getExpirationTimestamp(): Long

    interface FullScreenVideoAdInteractionListener {
        fun onAdShow()
        fun onAdVideoBarClick()
        fun onAdClose()
        fun onVideoComplete()
        fun onSkippedVideo()
    }

}