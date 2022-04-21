package com.dn.sdk.listener.draw.natives

import android.view.View
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTNativeAd
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 广告交互代理日子代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/25 16:12
 */
class LoggerAdInteractionListenerProxy(
    val adRequest: AdRequest,
    val countTrackImpl: CountTrackImpl,
    val listener: ITTNativeAd.AdInteractionListener?
) : ITTNativeAd.AdInteractionListener {
    override fun onAdClicked(var1: View?, var2: ITTNativeAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onAdClicked(${var1?.id})"))
        listener?.onAdClicked(var1, var2)
        countTrackImpl.onAdClick()
    }

    override fun onAdCreativeClick(var1: View?, var2: ITTNativeAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onAdCreativeClick(${var1?.id})"))
        listener?.onAdCreativeClick(var1, var2)
    }

    override fun onAdShow(var1: ITTNativeAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onAdShow()"))
        listener?.onAdShow(var1)
        countTrackImpl.onAdShow()
    }
}