package com.dn.sdk.listener.fullscreenvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.count.CountTrackImpl

/**
 * 全屏视频加载埋点代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/2 11:43
 */
class TrackFullScreenVideoLoadListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdFullScreenVideoLoadListener?
) : IAdFullScreenVideoLoadListener {

    val countTrack = CountTrackImpl(adRequest)

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }

    override fun onAdLoad(ad: ITTFullScreenVideoAdData) {
        listener?.onAdLoad(ad)
    }

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listener?.onAdStartLoad()
    }
}