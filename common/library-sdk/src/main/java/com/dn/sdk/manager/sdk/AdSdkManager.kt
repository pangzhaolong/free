package com.dn.sdk.manager.sdk

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Looper
import com.dn.sdk.BuildConfig
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.integral.DoNewsIntegralHolder
import com.donews.utilslibrary.utils.AppInfo

/**
 * 广告SDK 管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 14:07
 */
@SuppressLint("StaticFieldLeak")
object AdSdkManager : ISdkManager {

    private lateinit var context: Context

    @Volatile
    private var hadInit = false

    override fun initSDK(context: Application, channelName: String, openDebug: Boolean) {
        AdSdkManager.context = context.applicationContext
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            if (!hadInit) {
                AdLoggerUtils.setLoggable(BuildConfig.OPEN_AD_LOGGER)
                //多牛SDK 初始化
                initDnSdk(AdSdkManager.context, channelName)
                hadInit = true
            }
        }
    }

    fun getContext(): Context {
        return context
    }

    /** 初始化 多牛sdk */
    private fun initDnSdk(context: Context, channelName: String) {
        DoNewsAdManagerHolder.setChannel(channelName)
        //gromore初始化
        setUserInfo(AppInfo.getUserId(), AppInfo.getUserRegisterTime())
        DoNewsAdManagerHolder.init(context, BuildConfig.APP_ID_GROMORE)
        //积分墙SDK 初始化
        val uuid = DoNewsAdManagerHolder.getSuuid()
        DoNewsIntegralHolder.getInstance().init(context, channelName, uuid, BuildConfig.DEBUG)
    }

    fun setUserInfo(userId: String, registerTime: String) {
        DoNewsAdManagerHolder.setUserInfo(userId, registerTime)
    }
}