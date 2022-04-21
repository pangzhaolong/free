package com.dn.sdk.platform.donews.natives

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.INativeAdData
import com.dn.sdk.listener.feed.natives.IAdFeedListener
import com.dn.sdk.listener.feed.natives.LoggerFeedListenerProxy
import com.dn.sdk.loader.SdkType
import com.donews.ads.mediation.v2.framework.listener.DoNewsAdNativeData
import com.donews.ads.mediation.v2.framework.listener.NativeAdListener

/**
 * 多牛v2 信息流数据对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 15:09
 */
class DoNewsFeedData(
    private val adRequest: AdRequest,
    private val nativeData: DoNewsAdNativeData
) : INativeAdData {
    override fun getAdRequest(): AdRequest {
        return adRequest
    }

    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun getAdFrom(): Int {
        return nativeData.adFrom
    }

    override fun isAPP(): Boolean {
        return nativeData.isAPP
    }

    override fun getDesc(): String? {
        return nativeData.desc
    }

    override fun getTitle(): String? {
        return nativeData.title
    }

    override fun getImgUrl(): String? {
        return nativeData.imgUrl
    }

    override fun getIconUrl(): String? {
        return nativeData.iconUrl
    }

    override fun getLogoUrl(): String? {
        return nativeData.logoUrl
    }

    override fun getAdPatternType(): Int {
        return nativeData.adPatternType
    }

    override fun getVideoDuration(): Int {
        return nativeData.videoDuration
    }

    override fun getImgList(): List<String>? {
        return nativeData.imgList
    }

    override fun resume() {
        nativeData.resume()
    }

    override fun destroy() {
        nativeData.destroy()
    }

    override fun bindImageViews(list: MutableList<ImageView>?, var2: Int) {
        nativeData.bindImageViews(list, var2)
    }

    override fun bindView(
        context: Context,
        viewGroup: ViewGroup,
        frameLayout: FrameLayout,
        clickViews: List<View>?,
        listener: IAdFeedListener?
    ) {

        val loggerListener = LoggerFeedListenerProxy(adRequest, listener)
        nativeData.bindView(context, viewGroup, frameLayout, clickViews, object : NativeAdListener {
            override fun onAdStatus(code: Int, any: Any?) {
                loggerListener.onAdStatus(code, any)
            }

            override fun onAdExposure() {
                loggerListener.onAdExposure()
            }

            override fun onAdClicked() {
                loggerListener.onAdClicked()
            }

            override fun onAdError(errorMsg: String?) {
                loggerListener.onAdError(errorMsg)
            }
        })
    }
}