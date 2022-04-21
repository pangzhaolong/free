package com.dn.integral.bean

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.dn.integral.bean.IntegralAdData
import com.dn.sdk.listener.IDownloadAdListener
import com.donews.ads.mediation.v2.integral.DnIntegralAdListener
import com.donews.ads.mediation.v2.integral.DnIntegralNativeAd

/**
 *
 * 多牛v2 积分墙广告数据对象
 * @author: cymbi
 * @data: 2021/12/27
 */
class DoNewsIntegralAdData(
    private val nativeAd: DnIntegralNativeAd
) : IntegralAdData {

    override fun getAppName(): String {
        return nativeAd.appName
    }

    override fun getApkUrl(): String {
        return nativeAd.apkUrl
    }

    override fun getPkName(): String {
        return nativeAd.pkName
    }

    override fun getIcon(): String {
        return nativeAd.icon
    }

    override fun getPrice(): Int {
        return nativeAd.price
    }

    override fun getDeepLink(): String {
        return nativeAd.deepLink
    }

    override fun getDesc(): String {
        return nativeAd.desc
    }

    override fun getTaskType(): String {
        return nativeAd.taskType
    }

    override fun getWallRequestId(): String {
        return nativeAd.wallRequestId
    }

    override fun getSourceRequestId(): String {
        return nativeAd.sourceRequestId
    }

    override fun getSourcePlatform(): String {
        return nativeAd.sourcePlatform
    }

    override fun getSourceAdType(): String {
        return nativeAd.sourceAdType
    }

    override fun downLoadApk(context: Context?, immInstallApk: Boolean) {
        return nativeAd.downLoadApk(context, immInstallApk)
    }

    /**
     * 绑定视图
     *
     * @param context                 上下文
     * @param adContainer             广告容器
     * @param clickViews              需要点击的view列表
     * @param dnIntegralListener，监听事件
     */
    override fun bindView(
        context: Context?,
        adContainer: ViewGroup?,
        clickViews: List<View?>?,
        dnIntegralListener: IDownloadAdListener?
    ) {
        nativeAd.bindView(context, adContainer, clickViews, object : DnIntegralAdListener {
            override fun onAdShow() {
                dnIntegralListener?.onAdShow()
            }

            override fun onAdClick() {
                dnIntegralListener?.onAdClick()
            }

            override fun onStart() {
                dnIntegralListener?.onStart()
            }

            override fun onProgress(totalLength: Long, downloadedLength: Long) {
                dnIntegralListener?.onProgress(totalLength, downloadedLength)
            }

            override fun onComplete() {
                dnIntegralListener?.onComplete()
            }

            override fun onInstalled() {
                dnIntegralListener?.onInstalled()
            }

            override fun onRewardVerify() {
                dnIntegralListener?.onRewardVerify()
            }

            override fun onRewardVerifyError(errorMsg: String?) {
                dnIntegralListener?.onRewardVerifyError(errorMsg)
            }

            override fun onError(throwable: Throwable?) {
                dnIntegralListener?.onError(throwable)
            }
        })

    }

    /**
     * 主动启动第三方app的时候调用
     * 上报激活/次留激活
     */
    override fun reportActive() {
        nativeAd.reportActive()
    }

}