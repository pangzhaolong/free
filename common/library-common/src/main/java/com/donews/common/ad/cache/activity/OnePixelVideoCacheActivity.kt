package com.donews.common.ad.cache.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.sdk.sdk.ErrorConstant
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener
import com.dn.sdk.sdk.interfaces.view.PreloadVideoView
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.router.RouterActivityPath
import com.orhanobut.logger.Logger

/**
 * 一像素 activity用于缓存广告视频
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/24 9:32
 */
@Route(path = RouterActivityPath.Ad.AD_ONE_PX_CACHE_ACTIVITY)
class OnePixelVideoCacheActivity : AppCompatActivity() {

    companion object {
        var rewardVideoListener: IAdRewardVideoListener? = null
    }

    private var preloadVideoView: PreloadVideoView? = null


    @Autowired
    @JvmField
    var startCache: Boolean = false

    @Autowired
    @JvmField
    var videoType: Int = 0

    var needShow: Boolean = false

    /** 预加载错误，重试 */
    private var retry: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowOnePx()
        ARouter.getInstance().inject(this)
        if (startCache) {
            preloadRewardVideo()
        }
        moveTaskToBack(true)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        ARouter.getInstance().inject(this)
        if (videoType == 1) {
            needShow = false
            startPlayRewardVideo()
        }
    }


    private fun preloadRewardVideo() {
        AdManager.preloadRewardVideoAd(this, {
            preloadVideoView = it
            if (needShow) {
                preloadVideoView?.show()
                needShow = false
            }
        }, object : IAdRewardVideoListener {
            override fun onLoad() {
                rewardVideoListener?.onLoad()
                //加载成功,则重置加载次数
                retry = 0
            }

            override fun onLoadFail(code: Int, error: String?) {
                rewardVideoListener?.onLoadFail(code, error)
            }

            override fun onLoadTimeout() {
                rewardVideoListener?.onLoadTimeout()
            }

            override fun onLoadCached() {
                rewardVideoListener?.onLoadTimeout()
            }

            override fun onRewardAdShow() {
                rewardVideoListener?.onRewardAdShow()
            }

            override fun onRewardBarClick() {
                rewardVideoListener?.onRewardBarClick()
            }

            override fun onRewardedClosed() {
                moveTaskToBack(true)
                rewardVideoListener?.onRewardedClosed()
                rewardVideoListener = null
                preloadVideoView = null
                //加载下一个激励视频
                preloadRewardVideo()
            }

            override fun onRewardVideoComplete() {
                rewardVideoListener?.onRewardVideoComplete()
            }

            override fun onRewardVideoError() {
                rewardVideoListener?.onRewardVideoError()
            }

            override fun onRewardVideoAdShowFail(code: Int, message: String?) {
                rewardVideoListener?.onRewardVideoAdShowFail(code, message)
            }

            override fun onRewardVerify(result: Boolean) {
                rewardVideoListener?.onRewardVerify(result)
            }

            override fun onSkippedRewardVideo() {
                rewardVideoListener?.onSkippedRewardVideo()
            }

            override fun onError(code: Int, msg: String?) {
                //将此activity移入后台而不销毁
                rewardVideoListener?.onError(code, msg)
                preloadVideoView = null
                if (code != ErrorConstant.ERROR_CODE_NO_AD) {
                    retry++
                    if (retry < 3) {
                        preloadRewardVideo()
                    }
                }
            }
        })
    }

    private fun startPlayRewardVideo() {
        if (preloadVideoView == null) {
            needShow = true
            retry = 0
            preloadRewardVideo()
        } else {
            preloadVideoView?.show()
        }
    }

    private fun setWindowOnePx() {
        val window = window
        window.setGravity(Gravity.START or Gravity.TOP)
        val params: WindowManager.LayoutParams = window.attributes
        params.x = 0
        params.y = 0
        params.height = 1
        params.width = 1
        window.attributes = params
    }

}