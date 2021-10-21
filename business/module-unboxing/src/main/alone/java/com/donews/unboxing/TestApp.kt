package com.donews.unboxing

import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.base.BaseApplication
import com.donews.network.EasyHttp
import com.donews.network.cache.converter.GsonDiskConverter
import com.donews.network.cache.model.CacheMode
import com.donews.network.cookie.CookieManger
import com.donews.network.model.HttpHeaders
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.LogUtil

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/21 14:34
 */
class TestApp : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        if (LogUtil.allow) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
        EasyHttp.init(this)
        if (LogUtil.allow) {
            EasyHttp.getInstance().debug("HoneyLife", true)
        }
        val httpHeaders = HttpHeaders()
        httpHeaders.put(HttpHeaders.HEAD_AUTHORIZATION, AppInfo.getToken(""))
        EasyHttp.getInstance()
            .setBaseUrl(BuildConfig.BASE_CONFIG_URL)
            .setReadTimeOut((15 * 1000).toLong())
            .setWriteTimeOut((15 * 1000).toLong())
            .setConnectTimeout((15 * 1000).toLong())
            .setRetryCount(3)
            .setCookieStore(CookieManger(this))
            .setCacheDiskConverter(GsonDiskConverter())
            .setCacheMode(CacheMode.FIRSTREMOTE)
            .addCommonHeaders(httpHeaders)

    }
}