package com.dn.integral.bean

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.dn.sdk.listener.IDownloadAdListener
import com.donews.ads.mediation.v2.integral.DnIntegralAdListener


/**
 *
 * 积分墙对象
 * @author: cymbi
 * @data: 2021/12/27
 */
interface IntegralAdData {


    /**
     * app名字
     *
     * @return String
     */
    fun getAppName(): String?

    /**
     * apk下载地址
     *
     * @return String
     */
    fun getApkUrl(): String?

    /**
     * 包名
     *
     * @return String
     */
    fun getPkName(): String?

    /**
     * icon地址
     *
     * @return String
     */
    fun getIcon(): String?

    /**
     * 价格
     *
     * @return int
     */
    fun getPrice(): Int

    /**
     * deepLink
     *
     * @return String
     */
    fun getDeepLink(): String?

    /**
     * 广告描述
     *
     * @return String
     */
    fun getDesc(): String?

    /**
     * 激活状态
     * UNKNOWN_TASK: 未知
     * ACTIVATION_TASK: 激活任务
     * RETENTION_TASK: 次留任务
     *
     * @return
     */
    fun getTaskType(): String?

    /**
     * 广告唯一id
     *
     * @return String
     */
    fun getWallRequestId(): String?

    /**
     * 素材唯一id
     *
     * @return String
     */
    fun getSourceRequestId(): String?

    /**
     * 具体广告联盟平台类型
     * CHUANSHANJIA: 穿山甲
     * YOULIANGHUI: 优量汇
     * OPT_YOULIANGHUI: OPT优量汇
     * OPT_CHUANSHANJIA: OPT穿山甲
     * OPT_KUAISHOU: OPT快手
     *
     * @return String
     */
    fun getSourcePlatform(): String?

    /**
     * 广告类型
     * PLACE_ATTRIBUTE_UNKNOWN: 默认
     * LACE_ATTRIBUTE_SPLASH: 开屏
     * PLACE_ATTRIBUTE_FEED: 信息流
     * PLACE_ATTRIBUTE_BANNER: banner
     * PLACE_ATTRIBUTE_INTERSTITIAL: 插屏
     * PLACE_ATTRIBUTE_REWARDED_VIDEO: 激励视频
     *
     * @return String
     */
    fun getSourceAdType(): String?

    /**
     * 下载apk
     *
     * @param context
     * @param immInstallApk
     */
    fun downLoadApk(context: Context?, immInstallApk: Boolean)


    /**
     * 绑定视图
     *
     * @param context                 上下文
     * @param adContainer             广告容器
     * @param clickViews              需要点击的view列表
     * @param dnIntegralListener，监听事件
     */
    fun bindView(
        context: Context?,
        adContainer: ViewGroup?,
        clickViews: List<View?>?,
        dnIntegralListener: IDownloadAdListener?
    )

    /**
     * 主动启动第三方app的时候调用
     * 上报激活/次留激活
     */
    fun reportActive()
}