package com.donews.main.utils

import com.donews.main.BuildConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.CallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.JsonUtils
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils
import io.reactivex.disposables.Disposable

/**
 * @author lcl
 * Date on 2021/10/9
 * Description:
 * main模块中的配置API相关的帮助类
 */
object MainUrlApiHelper {
    /** 中台的基础地址 */
    private val BASE_URL: String = BuildConfig.BASE_CONFIG_URL

    /**
     * 启动页双开屏幕的配置
     * @param succ 获取配置信息成功
     * @param finish 请求处理完成。表示这个请求完成了处理。无论成功失败都会调用的
     */
    fun <T> getSplashDoubleScreenConfig(succ: (T) -> Unit, finish: () -> Unit = {}): Disposable {
        return EasyHttp.get(getConfigUrl("alkjdfklasjdkfl"))
            // .addInterceptor(new HttpLoggingInterceptor("AdSdkHttp")
            // .setLevel(HttpLoggingInterceptor.Level.BODY))
            .cacheMode(CacheMode.NO_CACHE)
            .isShowToast(BuildConfig.DEBUG)
            .execute(MainCallBack<T>(
                successCall = {
                    succ.invoke(it)
                },
                completedCall = {
                    finish.invoke()
                },
                errorCall = {
                    finish.invoke()
                }
            ))
    }

    /**
     * 获取指定的配置基础URL
     * @param appentUrl 追加接口信息
     * @return 基础地址
     */
    private fun getConfigUrl(appentUrl: String): String {
        var params = JsonUtils.getCommonJson(false)
        val tag = SPUtils.getInformain(KeySharePreferences.USER_TAG, null)
        if (tag != null) {
            params = "$params&$tag"
        }
        return "${BASE_URL}${appentUrl}${params}"
    }

    /**
     * 对网络接口回调的再次包装
     * @param T
     */
    class MainCallBack<T>(
        /**
         * 成功的回调
         */
        val successCall: (T) -> Unit,
        /**
         * 失败的回调
         */
        val errorCall: (ApiException?) -> Unit = {},
        /**
         * 返回数据data为空时候调用
         */
        val dataNullCall: () -> Unit = {},
        /**
         * 请求完成
         */
        val completedCall: () -> Unit = {}
    ) : CallBack<T>() {

        override fun onError(e: ApiException?) {
            errorCall.invoke(e)
        }

        override fun onSuccess(t: T) {
            successCall.invoke(t)
        }

        override fun onStart() {
        }

        override fun onCompleted() {
        }

        /**
         * 如果后台返回的data为空时走
         */
        override fun onCompleteOk() {
        }
    }
}