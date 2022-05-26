package com.donews.middle.adutils

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.feed.template.IAdFeedTemplateListener
import com.donews.yfsdk.loader.AdManager

object FeedNativeAndTemplateAd {

    fun loadFeedTemplateAd(
        activity: Activity?,
        widthDp: Float,
        heightDp: Float,
        listener: IAdFeedTemplateListener?
    ) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(
                AdCustomError.ContextError.code,
                AdCustomError.ContextError.errorMsg
            )
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadFeedTemplateAd(activity, widthDp, heightDp, listener)
    }

    /**
     * 扩展信息流加载
     * @param activity 上下文
     * @param adVp 显示信息流的容器
     * @param listener 监听。不关心可以为空
     */
    fun loadFeedTemplateAdNew(
        activity: Activity,
        adVp: ViewGroup,
        listener: IAdFeedTemplateListener?
    ) {
        val fListener = object : IAdFeedTemplateListener {
            override fun onAdStatus(code: Int, any: Any?) {
                listener?.onAdStatus(code, any)
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdStatus#code=$code,any=$any"
                )
            }

            /** 数据加载成功 */
            override fun onAdLoad(views: MutableList<View>) {
                listener?.onAdLoad(views)
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdLoad#views=$views"
                )
                if (!activity.isDestroyed && views.isNotEmpty()) {
                    Log.v(
                        "feedAd",
                        activity.javaClass.simpleName + " 信息流回调关联界面成功"
                    )
                    adVp.addView(views[0])
                }
            }

            /** 广告开始展示 */
            override fun onAdShow() {
                listener?.onAdShow()
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdShow"
                )
            }

            /** 广告曝光 */
            override fun onAdExposure() {
                listener?.onAdExposure()
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdExposure"
                )
            }

            /** 广告点击 */
            override fun onAdClicked() {
                listener?.onAdClicked()
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdClicked"
                )
            }

            /** 广告关闭*/
            override fun onAdClose() {
                listener?.onAdClose()
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdClose"
                )
            }

            /** 无广告填充 */
            override fun onAdError(code: Int, errorMsg: String?) {
                listener?.onAdError(code, errorMsg)
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdError#code=" + code + ",errorMsg=" + errorMsg
                )
            }

            override fun onAdStartLoad() {
                listener?.onAdStartLoad()
                Log.v(
                    "feedAd",
                    activity.javaClass.simpleName + " 信息流回调:onAdStartLoad"
                )
            }

        }
        //加载广告
        loadFeedTemplateAd(activity, 304F, 0F, fListener)
    }
}