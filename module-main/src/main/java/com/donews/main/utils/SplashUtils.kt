package com.donews.main.utils

import android.app.Activity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.dn.events.events.LoginUserStatus
import com.dn.events.events.NetworkChanageEvnet
import com.donews.base.utils.ToastUtil
import com.donews.common.contract.LoginHelp
import com.donews.main.common.CommonParams
import com.donews.main.entitys.resps.SplashDoubleADConfigResp
import com.donews.main.utils.SplashUtils.openLoginDeviceNotify
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 *  启动页的相关工具类
 */
object SplashUtils {
    /** 隐私协议的退出按钮的记录报错的key */
    private val PERSION_EXIT_SP_KEY = "PERSION_EXIT_SP_KEY"

    /** 开屏广告双屏配置 */
    var splashADConfig :SplashDoubleADConfigResp? = null
    /** 是否为冷启动的标志,T:是，F:否 */
    var isColdStart :Boolean = true

    //------------------------------ 以下是通知订阅方法 ------------------------------
    @Subscribe //网络状态变化监听
    fun eventNetworkChanage(event: NetworkChanageEvnet) {
        when (event.type) {
            0, 1, 2 -> {
                //只要网络连接状态变化。就尝试登录一次
                loginDevice()
            }
        }
    }

    @Subscribe //用户登录的通知
    fun eventUserLogin(event: LoginUserStatus) {
        LogUtils.v("用户登录状态：" + event.status)
        if (event.status == 1) {
            //用户已经登录成功了。那么取消注册通知
            EventBus.getDefault().unregister(this)
        }
    }

    //------------------------------ 以下是工具方法 ------------------------------

    /**
     * 获取开屏的双屏广告配置
     */
    fun getSplashDoubleADConfig(finish: () -> Unit = {}): Disposable {
        return MainUrlApiHelper.getSplashDoubleScreenConfig<SplashDoubleADConfigResp?>(succ = {
            splashADConfig = it
        }, finish = finish)
    }

    /**
     * 隐私协议中的退出应用
     * @param isReset 是否为重置
     */
    fun savePersonExit(isReset: Boolean = false) {
        if (isReset) {
            //重置为默认值(防止正常使用出现压榨视频)
            SPUtils.getInstance("dn_splash").put(PERSION_EXIT_SP_KEY, 0)
            return
        }
        val defV = SPUtils.getInstance("dn_splash").getInt(PERSION_EXIT_SP_KEY, 0)
        SPUtils.getInstance("dn_splash").put(PERSION_EXIT_SP_KEY, defV + 1)
    }

    /**
     * 检查隐私协议中的退出按钮是否已经达到需要播放广告的条件
     * @param activity Activity
     *
     * @return
     *  T:不重要用户，需要播放压榨激励视频了
     *  F:状态正常。不需要压榨
     */
    fun checkPersonExitStartAD(activity: Activity, finishTask: () -> Unit): Boolean {
        val defV = SPUtils.getInstance("dn_splash").getInt(PERSION_EXIT_SP_KEY, 0)
        if (defV >= 1) {
            //此处暂定未做任何事儿
            showAdRewardVideo(activity, finishTask)
            return true
        }
        return false
    }

    /**
     * 登录设备(以设备号进行登录)
     * 可通过[com.dn.events.events.LoginUserStatus]通知事件进行监听结果
     */
    fun loginDevice() {
        CommonParams.setNetWork()
    }

    /**
     * 开启登录设备通知。此方法再登录失败并且本地无可用用户信息时候使用
     *  因为所有信息和服务都需要依赖用户信息
     */
    fun openLoginDeviceNotify() {
        if (EventBus.getDefault().isRegistered(this)) {
            return //已经注册了
        }
        EventBus.getDefault().register(this)
    }

    /**
     * 播放激励视频
     * @param act Activity 上下文
     * @param finishTask 视频完成的处理逻辑(扩展关闭逻辑)
     */
    private fun showAdRewardVideo(act: Activity, finishTask: () -> Unit) {
        ToastUtil.show(act, "多次拒绝隐私协议")
        finishTask.invoke()
    }
}