package com.dn.sdk.listener

/**
 *  积分墙下载监听器
 *
 * @author: cymbi
 * @data: 2021/12/27
 */
interface IDownloadAdListener {

    fun onAdShow()

    /**
     * 广告点击，如果在监听到此点击事件，在这里通过包名或者deepLink启动第三方app，需要调用nativeAd.reportActive,上报激活，
     *  如果通过：nativeAd.downLoadApk(mContext, true)启动app，不需要调用需要调用nativeAd.reportActive
     */
    fun onAdClick()

    //开始下载
    fun onStart()

    //下载进度
    fun onProgress(totalLength: Long, downloadedLength: Long)

    //下载完成
    fun onComplete()

    //安装完成
    fun onInstalled()

    fun onRewardVerify()

    fun onRewardVerifyError(errorMsg: String?)

    fun onError(var1: Throwable?)
}
