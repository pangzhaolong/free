package com.dn.sdk.sdk

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Looper
import com.bytedance.sdk.openadsdk.TTAdManager
import com.dn.admediation.csj.api.DnMediationAdSdk
import com.dn.sdk.sdk.tt.TTAdManagerHolder
import com.donews.b.start.DoNewsAdManagerHolder
import com.donews.b.utils.DnLogSwitchUtils

/**
 * 广告SDK 管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 14:07
 */
@SuppressLint("StaticFieldLeak")
object AdSdkManager {

    private lateinit var context: Context

    /** 渠道信息 */
    private var channel: String? = null

    /** 用户信息 */
    private var userId: String? = null

    /** OAID 信息 */
    private var oaid: String? = null

    @Volatile
    private var hadInit = false

    fun initSDK(context: Application, openDebug: Boolean, groMoreAppId: String) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            this.context = context.applicationContext
            if (!hadInit) {
                //多牛SDK 初始化
                initDnSdk(this.context, openDebug)
                initDnGroMore(context, openDebug, groMoreAppId)
                hadInit = true
            }
        }
    }

    /** 初始化 多牛sdk */
    private fun initDnSdk(context: Context, openDebug: Boolean) {
        DnLogSwitchUtils.setEnableLog(openDebug)
        DoNewsAdManagerHolder.init(context)
    }

    /** 初始化多牛 GroMore */
    private fun initDnGroMore(context: Context, openDebug: Boolean, groMoreAppId: String) {
        TTAdManagerHolder.init(context, groMoreAppId, openDebug)
    }

    /** 初始化设置额外的信息 */
    fun setInfo(channel: String, userId: String, oaid: String) {
        this.channel = channel
        this.userId = userId
        this.oaid = oaid
        DnMediationAdSdk.setChannel(channel)
        DnMediationAdSdk.setUserId(userId)
        DnMediationAdSdk.setOAID(oaid)
    }

    fun getContext(): Context {
        return context
    }
}