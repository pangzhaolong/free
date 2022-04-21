package com.dn.sdk.listener.draw.natives

import android.view.View
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTNativeAd
import com.dn.sdk.count.CountTrackImpl

/**
 * 广告交互代理 埋点代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/2 11:34
 */
class TrackAdInteractionListenerProxy(
    val adRequest: AdRequest,
    val countTrackImpl: CountTrackImpl,
    val listener: ITTNativeAd.AdInteractionListener?
) : ITTNativeAd.AdInteractionListener {
    override fun onAdClicked(var1: View?, var2: ITTNativeAd?) {
        countTrackImpl.onAdClick()
        listener?.onAdClicked(var1, var2)
    }

    override fun onAdCreativeClick(var1: View?, var2: ITTNativeAd?) {
        listener?.onAdCreativeClick(var1, var2)
    }

    override fun onAdShow(var1: ITTNativeAd?) {
        countTrackImpl.onAdShow()
        listener?.onAdShow(var1)
    }
}