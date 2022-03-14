package com.donews.common.ad.cache

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadAd
import com.dn.sdk.listener.IAdRewardVideoListener
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.loader.IPreloadAdListener
import com.orhanobut.logger.Logger

/**
 * 预加载激励视频
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/17 18:00
 */
class PreloadRewardVideoInfo(private val activity: Activity?, private val invalid: Boolean) {


    companion object {
        const val PRELOAD_TIME_OUT = 15000
    }

    /** 预加载状态 */
    private var mPreloading: Boolean = false
    private var mNeedShow: Boolean = false
    private var mStartPreloadTime: Long = 0L

    private var mPreloadAd: PreloadAd? = null

    /** 广告回调 */
    private var mListener: IAdRewardVideoListener? = null


    private var mFailStatusPreloadNextAd: (() -> Unit)? = null
    private var mSuccessStatusPreloadNextAd: (() -> Unit)? = null

    fun preloadAd() {
        preloadAd(invalid)
    }

    fun showAd(listener: IAdRewardVideoListener?) {
        mListener = listener
        if (mPreloadAd == null) {
            if (mPreloading) {
                //正在预加载中，但是还没返回预加载对象
                mNeedShow = true
            } else {
                mListener?.onAdError(
                    AdCustomError.PreloadAdStatusError.code,
                    AdCustomError.PreloadAdStatusError.errorMsg
                )
                mListener = null
                mFailStatusPreloadNextAd?.invoke()
            }
        } else {
            mPreloadAd?.let {
                when (it.getLoadState()) {
                    PreloadAdState.Success -> {
                        it.showAd()
                    }
                    PreloadAdState.Loading -> {
                        val duration = System.currentTimeMillis() - mStartPreloadTime
                        if (duration >= PRELOAD_TIME_OUT) {
                            //直接返回错误，并且重新预加载一个视频
                            mListener?.onAdError(
                                AdCustomError.PreloadTimesError.code,
                                AdCustomError.PreloadTimesError.errorMsg
                            )
                            it.destroy()
                            mListener = null
                            mNeedShow = false
                            mFailStatusPreloadNextAd?.invoke()
                            tag("在加载状态,$duration 预加载时间过长。")
                        } else {
                            it.showAd()
                        }
                    }
                    else -> {
                        mListener?.onAdError(AdCustomError.PreloadAdStatusError.code, it.getLoadState().msg)
                        it.destroy()
                        mListener = null
                        mNeedShow = false
                        mFailStatusPreloadNextAd?.invoke()
                    }
                }
            }
        }
    }

    fun setPreloadNext(failPreload: () -> Unit, SuccessPreload: () -> Unit) {
        mFailStatusPreloadNextAd = failPreload
        mSuccessStatusPreloadNextAd = SuccessPreload
    }

    private fun preloadAd(invalid: Boolean) {
        if (activity == null) {
            mListener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        val preloadViewListener: IPreloadAdListener = object : IPreloadAdListener {
            override fun preloadAd(ad: PreloadAd) {
                mPreloadAd = ad
                if (mNeedShow) {
                    mPreloadAd?.showAd()
                    mNeedShow = false
                }
            }
        }

        val adRewardVideoListener: IAdRewardVideoListener = object : IAdRewardVideoListener {

            override fun onAdStartLoad() {
                mListener?.onAdStartLoad()
                tag("预加载激励视频开始------onAdStartLoad()")
            }

            override fun onAdStatus(code: Int, any: Any?) {
                mListener?.onAdStatus(code, any)
                tag("预加载激励视频状态------onAdStatus(${code},${any})")
            }

            override fun onAdLoad() {
                //加载成功,则重置加载次数
                mListener?.onAdLoad()
                tag("预加载激励视频成功------onAdLoad()")
            }

            override fun onVideoCached() {
                //加载成功,则重置加载次数
                mPreloading = false
                tag("预加载激励视频成功------onVideoCached()")
                mListener?.onVideoCached()
            }

            override fun onAdShow() {
                mListener?.onAdShow()
                tag("预加载激励视频展示------onAdShow()")
            }

            override fun onAdVideoClick() {
                mListener?.onAdVideoClick()
                tag("预加载激励视频被点击-----------onAdVideoClick()")
            }

            override fun onRewardVerify(result: Boolean) {
                mListener?.onRewardVerify(result)
                tag("预加载激励视频获取奖励回调-----------onRewardVerify($result)")
                mSuccessStatusPreloadNextAd?.invoke()
            }

            override fun onAdClose() {
                mListener?.onAdClose()
                mPreloadAd?.destroy()
                mPreloadAd = null
                mListener = null
                tag("预加载激励视频关闭-----------onAdClose()")
            }


            override fun onVideoComplete() {
                mListener?.onVideoComplete()
                tag("预加载激励视频播放完成-----------onVideoComplete()")
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                mPreloadAd = null
                if (mPreloading) {
                    mPreloading = false
                    if (code != AdCustomError.CloseAd.code) {
                        if (invalid) {
                            tag("预加载激励视频出现错误-----------onError($code,$errorMsg)，不再预加载")
                            mListener?.onAdError(
                                AdCustomError.PreloadAdEmptyError.code,
                                AdCustomError.PreloadAdEmptyError.errorMsg
                            )
                        } else {
                            tag("预加载激励视频出现错误-----------onError($code,$errorMsg),重新预加载无效激励。")
                            //如果预加载失败，则再用无效激励预加载一次
                            preloadAd(false)
                        }
                    } else {
                        tag("预加载激励视频出现错误-----------onError($code,$errorMsg)，广告关闭")
                        mListener?.onAdError(code, errorMsg)
                    }
                } else {
                    tag("预加载激励视频出现错误-----------onError($code,$errorMsg)，播放期间出现错误")
                    mListener?.onAdError(code, errorMsg)
                }
            }
        }

        if (!invalid) {
            mStartPreloadTime = System.currentTimeMillis()
            AdManager.preloadRewardVideoAd(activity, preloadViewListener, adRewardVideoListener)
        } else {
            mStartPreloadTime = System.currentTimeMillis()
            AdManager.preloadInvalidRewardVideoAd(activity, preloadViewListener, adRewardVideoListener)
        }
    }

    private fun tag(msg: String) {
        Logger.t("PreloadRewardVideoInfo").d(msg)
    }
}