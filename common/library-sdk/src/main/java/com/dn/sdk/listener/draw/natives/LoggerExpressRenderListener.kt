package com.dn.sdk.listener.draw.natives

import android.view.View
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTNativeAd
import com.dn.sdk.utils.AdLoggerUtils

/**
 * draw渲染
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/25 16:21
 */
class LoggerExpressRenderListener(
    val adRequest: AdRequest,
    val listener: ITTNativeAd.ExpressRenderListener?
) : ITTNativeAd.ExpressRenderListener {
    override fun onRenderSuccess(var1: View?, var2: Float, var3: Float, var4: Boolean) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd  onRenderSuccess(${var1?.id},$var2,$var3,$var4"))
        listener?.onRenderSuccess(var1, var2, var3, var4)
    }
}