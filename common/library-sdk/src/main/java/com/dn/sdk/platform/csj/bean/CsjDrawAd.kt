package com.dn.sdk.platform.csj.bean

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTDrawFeedAdData
import com.dn.sdk.bean.natives.ITTFeedAd
import com.dn.sdk.bean.natives.ITTNativeAd
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.draw.natives.*

/**
 * 穿山甲 自渲染draw流对象
 *
 *  make in st
 *  on 2022/1/13 11:48
 */
class CsjDrawAd(
    private val adRequest: AdRequest,
    private val countTrackImpl: CountTrackImpl,
    private val nativeData: TTDrawFeedAd
) : ITTDrawFeedAdData {
    override fun setCanInterruptVideoPlay(var1: Boolean) {
        nativeData.setCanInterruptVideoPlay(var1)
    }

    override fun setPauseIcon(var1: Bitmap?, var2: Int) {
        nativeData.setPauseIcon(var1, var2)
    }

    override fun setDrawVideoListener(var1: ITTDrawFeedAdData.DrawVideoListener?) {
        val proxyListener = LoggerDrawVideoListenerProxy(adRequest, var1)
        val listener = object : TTDrawFeedAd.DrawVideoListener {
            override fun onClickRetry() {
                proxyListener.onClickRetry()
            }

            override fun onClick() {
                proxyListener.onClick()
            }

        }
        nativeData.setDrawVideoListener(listener)
    }

    override fun setVideoAdListener(var1: ITTFeedAd.VideoAdListener?) {
        val proxyListener = LoggerVideoAdListenerProxy(adRequest, var1)

        val listener = object : TTFeedAd.VideoAdListener {
            override fun onVideoLoad(p0: TTFeedAd?) {
                proxyListener.onVideoLoad(p0)
            }

            override fun onVideoError(p0: Int, p1: Int) {
                proxyListener.onVideoError(p0, p1)
            }

            override fun onVideoAdStartPlay(p0: TTFeedAd?) {
                proxyListener.onVideoAdStartPlay(p0)
            }

            override fun onVideoAdPaused(p0: TTFeedAd?) {
                proxyListener.onVideoAdPaused(p0)
            }

            override fun onVideoAdContinuePlay(p0: TTFeedAd?) {
                proxyListener.onVideoAdContinuePlay(p0)
            }

            override fun onProgressUpdate(p0: Long, p1: Long) {
                proxyListener.onProgressUpdate(p0, p1)
            }

            override fun onVideoAdComplete(p0: TTFeedAd?) {
                proxyListener.onVideoAdComplete(p0)
            }
        }
        nativeData.setVideoAdListener(listener)
    }

    override fun getVideoDuration(): Double {
        return nativeData.videoDuration
    }

    override fun getCustomVideo(): ITTFeedAd.CustomizeVideo {
        val customizeVideo = object : ITTFeedAd.CustomizeVideo {
            override val videoUrl: String? = nativeData.customVideo.videoUrl

            override fun reportVideoStart() {
                nativeData.customVideo.reportVideoStart()
            }

            override fun reportVideoPause(var1: Long) {
                nativeData.customVideo.reportVideoStart()
            }

            override fun reportVideoContinue(var1: Long) {
                nativeData.customVideo.reportVideoContinue(var1)
            }

            override fun reportVideoFinish() {
                nativeData.customVideo.reportVideoFinish()
            }

            override fun reportVideoBreak(var1: Long) {
                nativeData.customVideo.reportVideoBreak(var1)
            }

            override fun reportVideoAutoStart() {
                nativeData.customVideo.reportVideoAutoStart()
            }

            override fun reportVideoStartError(var1: Int, var2: Int) {
                nativeData.customVideo.reportVideoStartError(var1, var2)
            }

            override fun reportVideoError(var1: Long, var3: Int, var4: Int) {
                nativeData.customVideo.reportVideoError(var1, var3, var4)
            }
        }
        return LoggerCustomizeVideoListenerProxy(adRequest, customizeVideo)
    }

    override fun getAdViewWidth(): Int {
        return nativeData.adViewWidth
    }

    override fun getAdViewHeight(): Int {
        return nativeData.adViewHeight
    }

    override fun destroy() {
        nativeData.destroy()
    }

    override fun getAdLogo(): Bitmap? {
        return nativeData.adLogo
    }

    override fun getAdView(): View? {
        return nativeData.adView
    }

    override fun getAppCommentNum(): Int {
        return nativeData.appCommentNum
    }

    override fun getAppScore(): Int {
        return nativeData.appScore
    }

    override fun getAppSize(): Int {
        return nativeData.appSize
    }

    override fun getButtonText(): String? {
        return nativeData.buttonText
    }

    override fun getDescription(): String? {
        return nativeData.description
    }

    override fun getDislikeDialog(var1: Activity?): TTAdDislike? {
        return nativeData.getDislikeDialog(var1)
    }

    override fun getDislikeDialog(var1: TTDislikeDialogAbstract?): TTAdDislike? {
        return nativeData.getDislikeDialog(var1)
    }

    override fun getDislikeInfo(): DislikeInfo? {
        return nativeData.dislikeInfo
    }

    override fun getDownloadStatusController(): DownloadStatusController? {
        return nativeData.downloadStatusController
    }

    override fun getIcon(): TTImage? {
        return nativeData.icon
    }

    override fun getImageList(): List<TTImage?>? {
        return nativeData.imageList
    }

    override fun getImageMode(): Int {
        return nativeData.imageMode
    }

    override fun getInteractionType(): Int {
        return nativeData.interactionType
    }

    override fun getMediaExtraInfo(): Map<String?, Any?>? {
        return nativeData.mediaExtraInfo
    }

    override fun getSource(): String? {
        return nativeData.source
    }

    override fun getTitle(): String? {
        return nativeData.title
    }

    override fun getVideoCoverImage(): TTImage? {
        return nativeData.videoCoverImage
    }

    override fun registerViewForInteraction(
        var1: ViewGroup,
        var2: View,
        var3: ITTNativeAd.AdInteractionListener?
    ) {
        val proxyListener = TrackAdInteractionListenerProxy(
            adRequest,
            countTrackImpl,
            LoggerAdInteractionListenerProxy(adRequest, countTrackImpl, var3)
        )

        val listener = object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdClicked(p0, this@CsjDrawAd)
            }

            override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdCreativeClick(p0, this@CsjDrawAd)
            }

            override fun onAdShow(p0: TTNativeAd?) {
                proxyListener.onAdShow(this@CsjDrawAd)
            }
        }
        nativeData.registerViewForInteraction(var1, var2, listener)
    }

    override fun registerViewForInteraction(
        var1: ViewGroup,
        var2: List<View?>,
        var3: List<View?>?,
        var4: ITTNativeAd.AdInteractionListener?
    ) {

        val proxyListener = TrackAdInteractionListenerProxy(
            adRequest,
            countTrackImpl,
            LoggerAdInteractionListenerProxy(adRequest, countTrackImpl, var4)
        )
        val listener = object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdClicked(p0, this@CsjDrawAd)
            }

            override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdCreativeClick(p0, this@CsjDrawAd)
            }

            override fun onAdShow(p0: TTNativeAd?) {
                proxyListener.onAdShow(this@CsjDrawAd)
            }
        }
        nativeData.registerViewForInteraction(var1, var2, var3, listener)
    }

    override fun registerViewForInteraction(
        var1: ViewGroup,
        var2: List<View?>,
        var3: List<View?>?,
        var4: View?,
        var5: ITTNativeAd.AdInteractionListener?
    ) {

        val proxyListener = TrackAdInteractionListenerProxy(
            adRequest,
            countTrackImpl,
            LoggerAdInteractionListenerProxy(adRequest, countTrackImpl, var5)
        )
        val listener = object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdClicked(p0, this@CsjDrawAd)
            }

            override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdCreativeClick(p0, this@CsjDrawAd)
            }

            override fun onAdShow(p0: TTNativeAd?) {
                proxyListener.onAdShow(this@CsjDrawAd)
            }
        }
        nativeData.registerViewForInteraction(var1, var2, var3, var4, listener)
    }

    override fun registerViewForInteraction(
        var1: ViewGroup,
        var2: List<View?>,
        var3: List<View?>,
        var4: List<View?>?,
        var5: View?,
        var6: ITTNativeAd.AdInteractionListener?
    ) {
        val proxyListener = TrackAdInteractionListenerProxy(
            adRequest,
            countTrackImpl,
            LoggerAdInteractionListenerProxy(adRequest, countTrackImpl, var6)
        )

        val listener = object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdClicked(p0, this@CsjDrawAd)
            }

            override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                proxyListener.onAdCreativeClick(p0, this@CsjDrawAd)
            }

            override fun onAdShow(p0: TTNativeAd?) {
                proxyListener.onAdShow(this@CsjDrawAd)
            }

        }
        nativeData.registerViewForInteraction(var1, var2, var3, listener)
    }

    override fun render() {
        nativeData.render()
    }

    override fun setActivityForDownloadApp(var1: Activity) {
        nativeData.setActivityForDownloadApp(var1)
    }

    override fun setDislikeCallback(
        var1: Activity?,
        var2: TTAdDislike.DislikeInteractionCallback?
    ) {
        val listener = object : TTAdDislike.DislikeInteractionCallback {
            override fun onShow() {
                var2?.onShow()
            }

            override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
                var2?.onSelected(p0, p1, p2)
            }

            override fun onCancel() {
                var2?.onCancel()
            }

        }
        nativeData.setDislikeCallback(var1, listener)
    }

    override fun setDislikeDialog(var1: TTDislikeDialogAbstract?) {
        nativeData.setDislikeDialog(var1)
    }

    override fun setDownloadListener(var1: TTAppDownloadListener?) {
        nativeData.setDownloadListener(var1)
    }

    override fun setExpressRenderListener(var1: ITTNativeAd.ExpressRenderListener?) {
        val proxyListener = LoggerExpressRenderListener(adRequest, var1)
        val listener =
            TTNativeAd.ExpressRenderListener { p0, p1, p2, p3 -> proxyListener.onRenderSuccess(p0, p1, p2, p3) }
        nativeData.setExpressRenderListener(listener)
    }

    override fun showInteractionExpressAd(var1: Activity?) {
        nativeData.showInteractionExpressAd(var1)
    }
}