package com.donews.main.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.base.AppManager
import com.donews.base.base.AppStatusConstant
import com.donews.base.base.AppStatusManager
import com.donews.common.router.RouterActivityPath
import com.donews.main.BuildConfig
import com.donews.main.dialog.ContinueLotteryDialog
import com.donews.main.dialog.NotLotteryDialog
import com.donews.main.dialog.OpenRedPacketDialog
import com.donews.main.dialog.RemindDialog
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

    private var notLotteryDialog: NotLotteryDialog? = null
    private var openRedPacketDialog: OpenRedPacketDialog? = null
    private var continueLotteryDialog: ContinueLotteryDialog? = null
    var remindDialog: RemindDialog? = null

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
            showContinueLotteryDialog(activity)
//            if (checkNotLottery()) {
//                showNotLotteryDialog(activity)
//            } else if (checkRedPacketNotOpen()) {
//                showOpenRedPacketDialog(activity)
//            } else {
//                showContinueLotteryDialog(activity)
//            }
        }
    }


    /**
     * 判断用户当日是否抽奖
     * @return Boolean true 抽过奖,false 未抽过奖
     */
    private fun checkNotLottery(): Boolean {
        //TODO 用户当日是否抽奖
        return true
    }

    /**
     * 判断还有红包未开启
     * @return Boolean true 当前有可以开但是没有开的红包
     */
    private fun checkRedPacketNotOpen(): Boolean {
        return true
    }


    /***
     *  显示未抽奖弹出框
     */
    private fun showNotLotteryDialog(activity: AppCompatActivity) {
        if (notLotteryDialog != null && notLotteryDialog!!.dialog != null && notLotteryDialog!!.dialog!!.isShowing) {
            return
        }
        notLotteryDialog = NotLotteryDialog.newInstance(exitInterceptConfig.notLotteryConfig)
            .apply {
                setOnDismissListener {
                    notLotteryDialog = null
                }
                setOnSureListener {
                    disMissDialog()
                    //TODO 跳转抽奖
                }
                setOnCloseListener {
                    disMissDialog()
                    exitApp(activity)
                }
            }
        notLotteryDialog?.show(activity.supportFragmentManager, NotLotteryDialog::class.simpleName)
    }

    /**
     * 开红包弹出框
     * @param activity activity
     */
    private fun showOpenRedPacketDialog(activity: AppCompatActivity) {
        if (openRedPacketDialog != null && openRedPacketDialog!!.dialog != null && openRedPacketDialog!!.dialog!!.isShowing) {
            return
        }
        openRedPacketDialog = OpenRedPacketDialog.newInstance(exitInterceptConfig.openRedPacketConfig)
            .apply {
                setOnDismissListener {
                    openRedPacketDialog = null
                }
                setOnSureListener {
                    disMissDialog()
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                        .withInt("position", 0)
                        .navigation()
                }
                setOnCancelListener {
                    disMissDialog()
                    showRemindDialog(activity)
                }
                setOnCloseListener {
                    disMissDialog()
                    showRemindDialog(activity)
                }
            }
        openRedPacketDialog?.show(activity.supportFragmentManager, OpenRedPacketDialog::class.simpleName)
    }


    /**
     * 继续抽奖弹出框
     * @param activity activity
     */
    private fun showContinueLotteryDialog(activity: AppCompatActivity) {
        if (continueLotteryDialog != null && continueLotteryDialog!!.dialog != null && continueLotteryDialog!!.dialog!!.isShowing) {
            return
        }
        continueLotteryDialog = ContinueLotteryDialog.newInstance(exitInterceptConfig.continueLotteryConfig)
            .apply {
                setOnDismissListener {
                    continueLotteryDialog = null
                }
                setOnSureListener {
                    disMissDialog()
                }
                setOnCloseListener {
                    disMissDialog()
                    showRemindDialog(activity)
                }
            }
        continueLotteryDialog?.show(activity.supportFragmentManager, ContinueLotteryDialog::class.simpleName)
    }


    /**
     * 显示提示弹出框
     * @param activity AppCompatActivity
     */
    private fun showRemindDialog(activity: AppCompatActivity) {
        if (remindDialog != null && remindDialog!!.dialog != null && remindDialog!!.dialog!!.isShowing) {
            return
        }
        remindDialog = RemindDialog.newInstance(exitInterceptConfig.remindConfig)
            .apply {
                setOnDismissListener {
                    remindDialog = null
                }
                setOnSureListener {
                    disMissDialog()
                    exitApp(activity)
                }

                setOnCancelListener {
                    disMissDialog()
                    exitApp(activity)
                }
            }
        remindDialog?.show(activity.supportFragmentManager, RemindDialog::class.simpleName)
    }

    /**
     *
     * @return String 弹窗中推荐商品的接口
     */
    fun getRecommendGoodsUrl(): String {
        return BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list"
    }

    /**
     * 退出app
     * @param activity AppCompatActivity
     */
    private fun exitApp(activity: AppCompatActivity) {
        // 程序关闭
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED)
        AnalysisUtils.onEvent(activity, AnalysisParam.SHUTDOWN)
        AppManager.getInstance().AppExit()
        activity.finish()
    }
}