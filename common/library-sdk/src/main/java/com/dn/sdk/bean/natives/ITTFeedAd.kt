package com.dn.sdk.bean.natives

import com.bytedance.sdk.openadsdk.TTFeedAd

/**
 * 自渲染基础广告信息
 *
 *  make in st
 *  on 2022/1/13 14:34
 */
interface ITTFeedAd: ITTNativeAd {

    fun setVideoAdListener(var1: VideoAdListener?)

    fun getVideoDuration(): Double

    fun getCustomVideo(): CustomizeVideo?

    fun getAdViewWidth(): Int

    fun getAdViewHeight(): Int

    interface CustomizeVideo {
        val videoUrl: String?
        fun reportVideoStart()
        fun reportVideoPause(var1: Long)
        fun reportVideoContinue(var1: Long)
        fun reportVideoFinish()
        fun reportVideoBreak(var1: Long)
        fun reportVideoAutoStart()
        fun reportVideoStartError(var1: Int, var2: Int)
        fun reportVideoError(var1: Long, var3: Int, var4: Int)
    }

    interface VideoAdListener {
        fun onVideoLoad(var1: TTFeedAd?)
        fun onVideoError(var1: Int, var2: Int)
        fun onVideoAdStartPlay(var1: TTFeedAd?)
        fun onVideoAdPaused(var1: TTFeedAd?)
        fun onVideoAdContinuePlay(var1: TTFeedAd?)
        fun onProgressUpdate(var1: Long, var3: Long)
        fun onVideoAdComplete(var1: TTFeedAd?)
    }

}