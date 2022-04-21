package com.dn.sdk.bean.natives

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.TTAdDislike.DislikeInteractionCallback

/**
 * 基础广告信息
 *
 *  make in st
 *  on 2022/1/13 14:22
 */
interface ITTNativeAd {

    fun destroy()

    fun getAdLogo(): Bitmap?

    fun getAdView(): View?

    fun getAppCommentNum(): Int

    fun getAppScore(): Int

    fun getAppSize(): Int

    fun getButtonText(): String?

    fun getDescription(): String?

    fun getDislikeDialog(var1: Activity?): TTAdDislike?

    fun getDislikeDialog(var1: TTDislikeDialogAbstract?): TTAdDislike?

    fun getDislikeInfo(): DislikeInfo?

    fun getDownloadStatusController(): DownloadStatusController?

    fun getIcon(): TTImage?

    fun getImageList(): List<TTImage?>?

    fun getImageMode(): Int

    fun getInteractionType(): Int

    fun getMediaExtraInfo(): Map<String?, Any?>?

    fun getSource(): String?

    fun getTitle(): String?

    fun getVideoCoverImage(): TTImage?

    fun registerViewForInteraction(var1: ViewGroup, var2: View, var3: AdInteractionListener?)

    fun registerViewForInteraction(
        var1: ViewGroup,
        var2: List<View?>,
        var3: List<View?>?,
        var4: AdInteractionListener?
    )

    fun registerViewForInteraction(
        var1: ViewGroup,
        var2: List<View?>,
        var3: List<View?>?,
        var4: View?,
        var5: AdInteractionListener?
    )

    fun registerViewForInteraction(
        var1: ViewGroup,
        var2: List<View?>,
        var3: List<View?>,
        var4: List<View?>?,
        var5: View?,
        var6: AdInteractionListener?
    )

    fun render()

    fun setActivityForDownloadApp(var1: Activity)

    fun setDislikeCallback(var1: Activity?, var2: DislikeInteractionCallback?)

    fun setDislikeDialog(var1: TTDislikeDialogAbstract?)

    fun setDownloadListener(var1: TTAppDownloadListener?)

    fun setExpressRenderListener(var1: ExpressRenderListener?)

    @MainThread
    fun showInteractionExpressAd(var1: Activity?)

    interface AdInteractionListener {
        fun onAdClicked(var1: View?, var2: ITTNativeAd?)
        fun onAdCreativeClick(var1: View?, var2: ITTNativeAd?)
        fun onAdShow(var1: ITTNativeAd?)
    }

    interface ExpressRenderListener {
        fun onRenderSuccess(var1: View?, var2: Float, var3: Float, var4: Boolean)
    }

}