package com.donews.common.appconfig

import com.donews.common.BuildConfig
import com.donews.common.bean.AppCommonConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.orhanobut.logger.Logger

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/28 11:18
 */
object AppCommonConfigUtils {

    private var commonConfig = AppCommonConfig()

    fun initConfig() {
        EasyHttp.get(BuildConfig.APP_COMMON_CONFIG)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<AppCommonConfig>() {
                override fun onError(e: ApiException?) {
                    Logger.e(e, "");
                }

                override fun onSuccess(t: AppCommonConfig?) {
                    Logger.d(t)
                    t?.let {
                        commonConfig = it
                    }
                }
            })
    }

    fun getConfig(): AppCommonConfig {
        return commonConfig;
    }
}