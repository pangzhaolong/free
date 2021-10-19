package com.donews.main.utils

import android.content.Context
import com.donews.main.BuildConfig
import com.donews.main.entitys.resps.ExitInterceptConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.orhanobut.logger.Logger

/**
 * 拦截退出
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/18 19:54
 */
object ExitInterceptUtils {

    private const val CONFIG_URL = BuildConfig.BASE_CONFIG_URL + "jdd-interceptExitConfig" + BuildConfig.BASE_RULE_URL

    private var exitInterceptConfig: ExitInterceptConfig = ExitInterceptConfig()

    private fun getInterceptConfig() {
        EasyHttp.get(CONFIG_URL)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<ExitInterceptConfig>() {
                override fun onError(e: ApiException?) {
                    Logger.e(e, "");
                }

                override fun onSuccess(t: ExitInterceptConfig?) {
                    t?.let {
                        exitInterceptConfig = it
                        Logger.d(it)
                    }
                }
            })
    }

    fun init() {
        getInterceptConfig()
    }

    fun intercept(context: Context) {
        if (checkNotLottery()) {
            showNotLotteryDialog(context)
        } else if (checkRedPacketNotOpen()) {
            showOpenRedPacketDialog(context)
        } else {
            showContinueLotteryDialog(context)
        }
    }


    /**
     * 判断用户当日是否抽奖
     * @return Boolean true 抽过奖,false 未抽过奖
     */
    private fun checkNotLottery(): Boolean {
        //TODO 用户当日是否抽奖
        return false
    }

    /**
     * 判断还有红包未开启
     * @return Boolean true 当前有可以开但是没有开的红包
     */
    private fun checkRedPacketNotOpen(): Boolean {
        return false
    }


    /***
     *  显示未抽奖弹出框
     */
    private fun showNotLotteryDialog(context: Context) {

    }

    /**
     * 开红包弹出框
     * @param context Context
     */
    private fun showOpenRedPacketDialog(context: Context) {

    }


    /**
     * 继续抽奖弹出框
     * @param context Context
     */
    private fun showContinueLotteryDialog(context: Context) {

    }
}