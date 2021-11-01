package com.donews.common.ad.business.monitor

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.donews.common.ad.business.callback.JddAdConfigManager
import com.donews.common.ad.business.loader.AdManager
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleInterstListener
import com.donews.common.contract.LoginHelp
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.AppStatusUtils
import com.orhanobut.logger.Logger
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.text.SimpleDateFormat
import java.util.*

/**
 * 页面监听,当一段时间后没有操作，则触发事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/27 17:35
 */
class PageMonitor : LifecycleObserver {

    private var mTag = PageMonitor::class.java.simpleName
    private val mHandler = Handler(Looper.getMainLooper())
    private val mDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

    //当前页面打开显示次数
    private var mResumeTimes = 0

    private val mBlackList = arrayListOf(
        "SplashActivity", "MainActivity", "TopFragment",
        "NorFragment", "FrontFragment", "LotteryActivity"
    )

    private var mResume = false

    private val runnable = Runnable {
        showAd()
    }


    var mActivity: AppCompatActivity? = null
    var mFragment: Fragment? = null

    fun attach(activity: AppCompatActivity, tag: String = activity::class.java.simpleName) {
        with(activity) {
            mTag = tag
            if (!mBlackList.contains(mTag)) {
                lifecycle.addObserver(this@PageMonitor)
                inject(this)
                mActivity = this
                Logger.d("attach ---- $mTag")
            }
        }
    }

    fun attach(fragment: Fragment, tag: String = fragment::class.java.simpleName) {
        with(fragment) {
            mTag = tag
            if (!mBlackList.contains(mTag)) {
                lifecycle.addObserver(this@PageMonitor)
                activity?.let {
                    inject(it)
                }
                mFragment = this
                Logger.d("attach ---- $mTag")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Logger.d("onCreate ----$mTag")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mResume = true
        mResumeTimes++
        PageCount.pageResume(mTag)

        resumeCheckShowAd()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mResume = false
        clearMessage()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        detach()
    }

    private fun clearMessage() {
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun sendMessage() {
        mHandler.removeCallbacksAndMessages(null)
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        mHandler.postDelayed(runnable, jddAdConfigBean.noOperationDuration * 1000L)
    }

    private fun detach() {
        mActivity?.lifecycle?.removeObserver(this)
        mFragment?.lifecycle?.removeObserver(this)
    }


    private fun resumeCheckShowAd() {
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        //注册时间大于设置时间
        if (checkUserRegisterTime(jddAdConfigBean.interstitialStartTime)) {
            //显示次数大于设置的页面最小显示次数,比如第二次显示页面才显示广告
            val resumeNumber = PageCount.getResumeNumber(mTag)
            val showAdNumber = PageCount.getAdShowNumber(mTag)
            //显示广告次数小于配置的最大次数
            if (showAdNumber < jddAdConfigBean.pageInterstitialShowTimes) {
                //页面显示次数必须大于配置的最小次数
                if (resumeNumber >= jddAdConfigBean.pageShowTimes) {
                    //第一次显示页面，直接显示广告
                    if (mResumeTimes == 1) {
                        showAd()
                    } else {
                        sendMessage()
                    }
                }
            }
        }
    }


    private fun checkSendMessage() {
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        //注册时间大于设置时间
        if (checkUserRegisterTime(jddAdConfigBean.interstitialStartTime)) {
            val showAdNumber = PageCount.getAdShowNumber(mTag)
            if (showAdNumber < jddAdConfigBean.pageInterstitialShowTimes) {
                sendMessage()
            }
        }
    }


    /** 如果没有登录，用第一次打开app时间进行判断,否则使用注册时间进行判断 */
    private fun checkUserRegisterTime(time: Int): Boolean {
        val duration = time * 60 * 60 * 1000L

        if (LoginHelp.getInstance().isLogin) {
            val installApp = AppStatusUtils.getAppInstallTime()
            if (System.currentTimeMillis() - installApp >= duration) {
                return true
            }
            return false
        } else {
            return try {
                val bean = LoginHelp.getInstance().userInfoBean
                val createAt = bean.createdAt
                val registerDate = mDataFormat.parse(createAt)
                registerDate?.let {
                    (System.currentTimeMillis() - it.time) >= duration
                } ?: kotlin.run { false }
            } catch (e: Exception) {
                val installApp = AppStatusUtils.getAppInstallTime();
                if (System.currentTimeMillis() - installApp >= duration) {
                    return true
                }
                return false
            }
        }
    }

    private fun showAd() {
        //显示次数小于最大显示次数
        val activity = mActivity ?: mFragment?.activity
        activity?.let {
            AdManager.loadInterstitialAd(it, object : SimpleInterstListener() {
                override fun onError(code: Int, msg: String?) {
                    super.onError(code, msg)
                    Logger.d("${mTag}加载广告错误---- code = $code ,msg =  $msg ");
                    checkSendMessage()
                }


                override fun onAdClosed() {
                    super.onAdClosed()
                    PageCount.showAdSuccess(mTag)
                    checkSendMessage()
                }
            })
        }
    }


    /** 使用动态代理的形式监听界面的 触摸操作事件*/
    private fun inject(activity: Activity) {
        val window = activity.window
        val callback = window.callback
        val handler = WindowCallbackInvocation(callback)
        val proxy: Window.Callback = Proxy.newProxyInstance(
            Window.Callback::class.java.classLoader,
            arrayOf(Window.Callback::class.java), handler
        ) as Window.Callback
        window.callback = proxy
    }

    inner class WindowCallbackInvocation(val callback: Any) : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            if ("dispatchTouchEvent" == method?.name) {
                val event: MotionEvent = args?.get(0) as MotionEvent
                if (MotionEvent.ACTION_DOWN == event.action) {
                    clearMessage()
                }
                if (MotionEvent.ACTION_UP == event.action) {
                    if (mResume) {
                        checkSendMessage()
                    }
                }
            }
            return method?.invoke(callback, *(args ?: arrayOfNulls<Any>(0)))
        }
    }
}