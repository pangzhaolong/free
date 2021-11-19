package com.donews.main.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleInterstListener
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleRewardVideoListener
import com.donews.base.base.AppManager
import com.donews.base.base.AppStatusConstant
import com.donews.base.base.AppStatusManager
import com.donews.base.utils.ToastUtil
import com.donews.common.ad.business.callback.JddAdConfigManager
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.monitor.LotteryAdCount
import com.donews.common.router.RouterActivityPath
import com.donews.common.router.RouterFragmentPath
import com.donews.main.BuildConfig
import com.donews.main.dialog.*
import com.donews.main.entitys.resps.ExitInterceptConfig
import com.donews.middle.abswitch.ABSwitch
import com.donews.middle.bean.HighValueGoodsBean
import com.donews.middle.cache.GoodsCache
import com.donews.middle.request.RequestUtil
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.analysis.AnalysisParam
import com.donews.utilslibrary.analysis.AnalysisUtils
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils
import com.donews.utilslibrary.utils.withConfigParams
import com.orhanobut.logger.Logger

/**
 * 拦截退出
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/18 19:54
 */
object ExitInterceptUtils {

    private const val CONFIG_URL =
        BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + "-interceptExitConfig" + BuildConfig.BASE_RULE_URL

    /** 兩次返回鍵的间隔时间 */
    private const val CLICK_INTERVAL: Long = 2000L

    var exitInterceptConfig: ExitInterceptConfig = ExitInterceptConfig()

    private var mFirstClickBackTime: Long = 0L

    private var notLotteryDialog: NotLotteryDialog? = null

    //未登录的弹窗
    private var notLoginDialog: ExitNotLoginDialog? = null

    //中奖弹窗
    private var winningDialog: ExitWinningDialog? = null

    //有红包未开启的弹窗
    private var openRedPacketDialog: OpenRedPacketDialog? = null

    //红包全开的弹窗
    private var redPacketAllOpenDialog: RedPacketAllOpenDialog? = null

    //红包全开的弹窗
    private var redPacketNotAllOpenDialog: WinNotAllOpenDialog? = null

    //已登录未抽奖的弹窗
    private var continueLotteryDialog: ContinueLotteryDialog? = null
    var remindDialog: RemindDialog? = null

    private fun getInterceptConfig() {
        EasyHttp.get(CONFIG_URL.withConfigParams())
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
        val duration = System.currentTimeMillis() - mFirstClickBackTime
        if (duration < CLICK_INTERVAL) {
            // 程序关闭
            if (!exitInterceptConfig.intercept) {
                exitApp(activity)
            } else {
                if (!checkUserIsLogin()) {
                    //用户未登录
                    showNotLoginDialog(activity)
                    return
                }
                //已登录的逻辑判断
                // 1:已登录未抽奖
                if (checkNotLottery()) { //判断当日是否抽奖
                    //当日未抽奖
                    showContinueLotteryDialog(activity)
                    return
                }
                // 2：已登录，已抽奖,判断抽奖是否达到了10次                                                                                                                                                                                                                      
                if (getNotLotteryCount() >= 10) {
                    if (checkRedPacketNotOpen()) {
                        //有红包未开启
                        showOpenRedPacketDialog(activity)
                    } else {
                        //红包已全部开启
                        showRedPacketAllOpenDialog(activity)
                    }
                } else {
                    // 抽奖未达到10次
                    showWinNotAllOpenDialog(activity)
                }
//                showNotLotteryDialog(activity)
            }
        } else {
            Toast.makeText(activity.application, "再按一次退出！", Toast.LENGTH_SHORT).show()
            mFirstClickBackTime = System.currentTimeMillis()
        }
    }


    /**
     * 判断用户是否已登录
     * @return Boolean T 已登录,F 未登录
     */
    private fun checkUserIsLogin(): Boolean {
        return AppInfo.checkIsWXLogin()
    }

    /**
     * 判断用户当日是否抽奖
     * @return Boolean false 抽过奖,true 未抽过奖
     */
    private fun checkNotLottery(): Boolean {
        return !LotteryAdCount.todayLottery()
    }

    /**
     * 获取今日参与抽奖的次数
     * @return 抽奖次数
     */
    private fun getNotLotteryCount(): Int {
        return LotteryAdCount.getTodayLotteryCount()
    }

    /**
     * 判断还有红包未开启
     * @return Boolean true 当前有可以开但是没有开的红包
     */
    private fun checkRedPacketNotOpen(): Boolean {
        val number = SPUtils.getInformain(KeySharePreferences.CLOSE_RED_PACKAGE_COUNTS, 0);
        if (number > 0) {
            return true
        }
        return false
    }

    /***
     *  显示未抽奖弹出框
     */
    private fun showNotLoginDialog(activity: AppCompatActivity) {
        if (notLoginDialog != null && notLoginDialog!!.isShowing) {
            return
        }
        notLoginDialog = ExitNotLoginDialog.newInstance(activity)
        notLoginDialog!!.setFinishListener(object : ExitNotLoginDialog.OnFinishListener {
            override fun onDismiss() {
                notLoginDialog = null
            }

            override fun onDismissAd() {
                notLoginDialog = null
            }
        })
        notLoginDialog?.show()
    }

    /***
     *  显示未抽奖弹出框
     */
    private fun showWinningDialog(activity: AppCompatActivity) {
        if (winningDialog != null && winningDialog!!.dialog != null && winningDialog!!.dialog!!.isShowing) {
            return
        }
        winningDialog = ExitWinningDialog.newInstance().apply {
            setOnDismissListener {
                notLotteryDialog = null
            }
            setOnSureListener {
                RequestUtil.requestHighValueGoodsInfo()
                ARouter.getInstance()
                    .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                    .withString("goods_id", item!!.goodsId)
                    .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery)
                    .navigation()
                disMissDialog()
            }
            setOnCloseListener {
                RequestUtil.requestHighValueGoodsInfo()
                disMissDialog()
//                exitApp(activity)
            }
            setOnLaterListener {
                RequestUtil.requestHighValueGoodsInfo()
                disMissDialog()
            }
        }
        winningDialog?.show(activity.supportFragmentManager, NotLotteryDialog::class.simpleName)
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
                }
                setOnCloseListener {
                    disMissDialog()
                    notLotteryExitApp(activity)
                }
            }
        notLotteryDialog?.show(activity.supportFragmentManager, NotLotteryDialog::class.simpleName)
    }

    /**
     * 参与了抽奖但是还有开红包弹出框
     * @param activity activity
     */
    private fun showOpenRedPacketDialog(activity: AppCompatActivity) {
        if (openRedPacketDialog != null && openRedPacketDialog!!.dialog != null && openRedPacketDialog!!.dialog!!.isShowing) {
            return
        }
        openRedPacketDialog =
            OpenRedPacketDialog.newInstance(exitInterceptConfig.openRedPacketConfig)
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
//                        showRemindDialog(activity)
                    }
                    setOnCloseListener {
                        disMissDialog()
//                        showRemindDialog(activity)
                    }
                    setOnLaterListener {
                        disMissDialog()
                        exitApp(activity)
                    }
                }
        openRedPacketDialog?.show(
            activity.supportFragmentManager,
            OpenRedPacketDialog::class.simpleName
        )
    }

    /**
     * 参与了抽奖,但是抽奖不到10次
     * @param activity activity
     */
    private fun showWinNotAllOpenDialog(activity: AppCompatActivity) {
        if (redPacketNotAllOpenDialog != null && redPacketNotAllOpenDialog!!.dialog != null && redPacketNotAllOpenDialog!!.dialog!!.isShowing) {
            return
        }
        redPacketNotAllOpenDialog =
            WinNotAllOpenDialog.newInstance()
                .apply {
                    setOnDismissListener {
                        redPacketNotAllOpenDialog = null
                    }
                    setOnSureListener {
                        val item: HighValueGoodsBean? =
                            GoodsCache.readGoodsBean(HighValueGoodsBean::class.java, "exit")
                        disMissDialog()
                        if (item != null) {
                            showWinningDialog(activity)
                        }
                    }
                    setOnCancelListener {
                        disMissDialog()
                    }
                    setOnCloseListener {
                        disMissDialog()
                    }
                    setOnLaterListener {
                        disMissDialog()
                        if (checkRedPacketNotOpen()) {
                            //有红包未开启
                            showOpenRedPacketDialog(activity)
                        } else {
                            //红包已全部开启(显示开奖提醒)
                            showRemindDialog(activity)
                        }
                    }
                }
        redPacketNotAllOpenDialog?.show(
            activity.supportFragmentManager,
            WinNotAllOpenDialog::class.simpleName
        )
    }

    /**
     * 参与了抽奖,并且红包已经全部开启了
     * @param activity activity
     */
    private fun showRedPacketAllOpenDialog(activity: AppCompatActivity) {
        if (redPacketAllOpenDialog != null && redPacketAllOpenDialog!!.dialog != null && redPacketAllOpenDialog!!.dialog!!.isShowing) {
            return
        }
        redPacketAllOpenDialog =
            RedPacketAllOpenDialog.newInstance()
                .apply {
                    setOnDismissListener {
                        redPacketAllOpenDialog = null
                    }
                    setOnSureListener {
                        disMissDialog()
                    }
                    setOnCancelListener {
                        disMissDialog()
                    }
                    setOnCloseListener {
                        disMissDialog()
                    }
                    setOnLaterListener {
                        disMissDialog()
                        exitApp(activity)
                    }
                }
        redPacketAllOpenDialog?.show(
            activity.supportFragmentManager,
            RedPacketAllOpenDialog::class.simpleName
        )
    }


    /**
     * 已登录用户。当日未参与抽奖的弹窗
     * @param activity activity
     */
    private fun showContinueLotteryDialog(activity: AppCompatActivity) {
        if (continueLotteryDialog != null && continueLotteryDialog!!.dialog != null && continueLotteryDialog!!.dialog!!.isShowing) {
            return
        }
        continueLotteryDialog =
            ContinueLotteryDialog.newInstance(exitInterceptConfig.continueLotteryConfig)
                .apply {
                    setOnDismissListener {
                        continueLotteryDialog = null
                    }
                    setOnSureListener {
                        disMissDialog()
                    }
                    setOnCloseListener {
                        disMissDialog()
//                        showRemindDialog(activity)
                    }
                    setOnLaterListener {
                        disMissDialog()
                        exitApp(activity)
                    }
                }
        continueLotteryDialog?.show(
            activity.supportFragmentManager,
            ContinueLotteryDialog::class.simpleName
        )
    }


    /**
     * 显示提示弹出框(是否开启提醒、明日开奖的提示框)
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

                setOnCloseListener {
                    disMissDialog()
                }

                setOnLaterListener {
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


    private fun notLotteryExitApp(activity: AppCompatActivity) {
        JddAdConfigManager.addListener {
            val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
            if (jddAdConfigBean.notLotteryExitAppDialogAdEnable) {
                if (jddAdConfigBean.notLotteryExitAppDialogAdType == 1) {
                    AdManager.loadInterstitialAd(activity, object : SimpleInterstListener() {
                        override fun onError(code: Int, msg: String?) {
                            super.onError(code, msg)
                            if (jddAdConfigBean.notLotteryExitAppDialogAdMutex) {
                                realExitApp(activity)
                            } else {
                                exitApp(activity)
                            }
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                            if (jddAdConfigBean.notLotteryExitAppDialogAdMutex) {
                                realExitApp(activity)
                            } else {
                                exitApp(activity)
                            }
                        }
                    })
                } else {
                    AdManager.loadRewardVideoAd(activity, object : SimpleRewardVideoListener() {
                        override fun onError(code: Int, msg: String?) {
                            super.onError(code, msg)
                            if (jddAdConfigBean.notLotteryExitAppDialogAdMutex) {
                                realExitApp(activity)
                            } else {
                                exitApp(activity)
                            }
                        }

                        override fun onRewardedClosed() {
                            super.onRewardedClosed()
                            if (jddAdConfigBean.notLotteryExitAppDialogAdMutex) {
                                realExitApp(activity)
                            } else {
                                exitApp(activity)
                            }
                        }
                    })
                }
            } else {
                exitApp(activity)
            }
        }
    }

    /**
     * 退出app
     * @param activity AppCompatActivity
     */
    @JvmStatic
    fun exitApp(activity: AppCompatActivity) {
        LotteryAdCount.exitAppWithNotLottery()
        JddAdConfigManager.addListener {
            val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
            val times = LotteryAdCount.getExitAppWithNotLotteryTimes()
            if (times >= jddAdConfigBean.notLotteryExitAppTimes) {
                LotteryAdCount.resetExitAppWithNotLotteryTimes()
                if (jddAdConfigBean.notLotteryExitAppAdType == 1) {
                    AdManager.loadInterstitialAd(activity, object : SimpleInterstListener() {
                        override fun onError(code: Int, msg: String?) {
                            super.onError(code, msg)
                            realExitApp(activity)
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                            realExitApp(activity)
                        }
                    })
                } else {
                    AdManager.loadRewardVideoAd(activity, object : SimpleRewardVideoListener() {
                        override fun onError(code: Int, msg: String?) {
                            super.onError(code, msg)
                            realExitApp(activity)
                        }

                        override fun onRewardedClosed() {
                            super.onRewardedClosed()
                            realExitApp(activity)
                        }
                    })
                }

            } else {
                realExitApp(activity)
            }
        }
    }

    private fun realExitApp(activity: AppCompatActivity) {
        // 程序关闭
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED)
        AnalysisUtils.onEvent(activity, AnalysisParam.SHUTDOWN)
        AppManager.getInstance().AppExit()
        activity.finish()
    }
}