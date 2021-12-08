package com.dn.sdk.bean.preload

import com.dn.sdk.bean.AdType
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.loader.SdkType
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 预加载广告积累
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:32
 */
abstract class PreloadAd {
    private var mLoadState: PreloadAdState = PreloadAdState.Init
    private var mNeedShow: Boolean = false

    fun showAd() {
        when (mLoadState) {
            PreloadAdState.Init -> {
                AdLoggerUtils.d("预加载${getAdType().msg}状态错误,当前预加载广告对象处于${PreloadAdState.Init.msg}")
            }
            PreloadAdState.Error -> {
                AdLoggerUtils.d("预加载${getAdType().msg}状态错误,当前预加载广告对象处于${PreloadAdState.Error.msg}")
            }
            PreloadAdState.Destroy -> {
                AdLoggerUtils.d("预加载${getAdType().msg}状态错误,当前预加载广告对象处于${PreloadAdState.Destroy.msg}")
            }
            PreloadAdState.Loading -> {
                mNeedShow = true
                AdLoggerUtils.d("预加载${getAdType().msg}加载中,当前预加载广告对象处于${PreloadAdState.Loading.msg}，设置了mNeedShow is true")
            }
            PreloadAdState.Success -> {
                AdLoggerUtils.d("预加载${getAdType().msg}加载成功,正常播放广告")
                mNeedShow = false
                realShowAd()
                mLoadState = PreloadAdState.Shown
            }
            PreloadAdState.Shown -> {
                AdLoggerUtils.d("预加载${getAdType().msg}已经被展示过了")
            }
        }
    }

    fun destroy() {
        mLoadState = PreloadAdState.Destroy
        realDestroy()
    }

    fun isNeedShow(): Boolean {
        return mNeedShow
    }

    fun getLoadState(): PreloadAdState {
        return mLoadState
    }

    fun setLoadState(state: PreloadAdState) {
        mLoadState = state
    }

    /** 预加载的广告类型 */
    abstract fun getAdType(): AdType

    /** 返回广告平台类型 */
    abstract fun getSdkType(): SdkType

    /** 实际显示广告对象 */
    protected abstract fun realShowAd()


    /** 销毁广告 */
    protected abstract fun realDestroy()
}