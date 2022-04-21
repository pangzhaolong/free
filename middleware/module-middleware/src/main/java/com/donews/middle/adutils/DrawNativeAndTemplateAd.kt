package com.donews.middle.adutils

import android.app.Activity
import android.util.Log
import android.view.View
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.natives.ITTDrawFeedAdData
import com.dn.sdk.bean.natives.ITTNativeExpressAdData
import com.dn.sdk.listener.draw.natives.IAdDrawNativeLoadListener
import com.dn.sdk.listener.draw.template.IAdDrawTemplateLoadListener
import com.donews.yfsdk.loader.AdManager

object DrawNativeAndTemplateAd {

    fun loadDrawNativeAd(activity: Activity?, listenerNative: IAdDrawNativeLoadListener?) {
        if (activity == null || activity.isFinishing) {
            listenerNative?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadDrawNativeAd(activity, listenerNative)
    }

    fun loadDrawTemplateAd(activity: Activity?, listener: IAdDrawTemplateLoadListener?) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadDrawTemplateAd(activity, listener)
    }

    fun startDrawTemplateAd(activity: Activity, adCall: (view: View?) -> Unit = {}) {
        loadDrawTemplateAd(activity, object : IAdDrawTemplateLoadListener {
            override fun onAdError(code: Int, errorMsg: String?) {

            }

            override fun onAdLoad(list: List<ITTNativeExpressAdData>) {
                val ad = list[0]
                ad.setVideoAdListener(object : ITTNativeExpressAdData.ExpressVideoAdListener {
                    override fun onVideoLoad() {

                    }

                    override fun onVideoError(errorCode: Int, extraCode: Int) {

                    }

                    override fun onVideoAdStartPlay() {

                    }

                    override fun onVideoAdPaused() {

                    }

                    override fun onVideoAdContinuePlay() {

                    }

                    override fun onProgressUpdate(current: Long, duration: Long) {

                    }

                    override fun onVideoAdComplete() {

                    }

                    override fun onClickRetry() {

                    }
                })
                ad.setExpressInteractionListener(object :
                        ITTNativeExpressAdData.ExpressAdInteractionListener {
                    override fun onAdClicked(view: View?, type: Int) {

                    }

                    override fun onAdShow(view: View?, type: Int) {

                    }

                    override fun onRenderFail(view: View?, msg: String?, code: Int) {

                    }

                    override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                        view?.let {
                            adCall.invoke(view)
                        }
                    }

                })
                ad.render()
            }

            override fun onAdStartLoad() {

            }
        })
    }

    fun startDrawNativeAd(activity: Activity, adCall: (ad: ITTDrawFeedAdData?) -> Unit = {}) {
        loadDrawNativeAd(activity, object : IAdDrawNativeLoadListener {
            override fun onAdError(code: Int, errorMsg: String?) {
                Log.i("testError-->", "-onAdError->${code},${errorMsg}")
            }

            override fun onAdLoad(list: List<ITTDrawFeedAdData>) {
                Log.i("testError-->", "-onAdLoad->")
                val ad = list[0]
                ad.setActivityForDownloadApp(activity)
                ad.setDrawVideoListener(object : ITTDrawFeedAdData.DrawVideoListener {
                    override fun onClickRetry() {}

                    override fun onClick() {}

                })
                ad.setCanInterruptVideoPlay(true)
                adCall.invoke(ad)
            }

            override fun onAdStartLoad() {

            }
        })
    }
}