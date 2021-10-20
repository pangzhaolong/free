package com.donews.main.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donews.base.base.AppManager
import com.donews.base.base.AppStatusConstant
import com.donews.base.base.AppStatusManager
import com.donews.main.BuildConfig
import com.donews.main.dialog.NotLotteryDialog
import com.donews.main.entitys.resps.ExitInterceptConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.analysis.AnalysisParam
import com.donews.utilslibrary.analysis.AnalysisUtils
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

    /** 兩次返回鍵的间隔时间 */
    private const val CLICK_INTERVAL: Long = 2000L

    private var exitInterceptConfig: ExitInterceptConfig = ExitInterceptConfig()

    private var mFirstClickBackTime: Long = 0L

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

    fun intercept(activity: AppCompatActivity) {
        if (!exitInterceptConfig.intercept) {
            val duration = System.currentTimeMillis() - mFirstClickBackTime
            if (duration < CLICK_INTERVAL) {
                // 程序关闭
                AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED)
                AnalysisUtils.onEvent(activity, AnalysisParam.SHUTDOWN)
                AppManager.getInstance().AppExit()
                activity.finish()
            } else {
                Toast.makeText(activity.application, "再按一次退出！", Toast.LENGTH_SHORT).show()
                mFirstClickBackTime = System.currentTimeMillis()
            }
        } else {
            if (checkNotLottery()) {
                showNotLotteryDialog(activity)
            } else if (checkRedPacketNotOpen()) {
                showOpenRedPacketDialog(activity)
            } else {
                showContinueLotteryDialog(activity)
            }
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
    private fun showNotLotteryDialog(activity: AppCompatActivity) {
        val dialog = NotLotteryDialog.newInstance(exitInterceptConfig.notLotteryConfig)
        dialog.show(activity.supportFragmentManager, NotLotteryDialog::class.java.simpleName)
    }

    /**
     * 开红包弹出框
     * @param activity activity
     */
    private fun showOpenRedPacketDialog(activity: AppCompatActivity) {

    }


    /**
     * 继续抽奖弹出框
     * @param activity activity
     */
    private fun showContinueLotteryDialog(activity: AppCompatActivity) {

    }
}