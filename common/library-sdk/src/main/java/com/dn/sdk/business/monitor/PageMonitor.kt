package com.dn.sdk.business.monitor

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
import com.dn.sdk.business.callback.JddAdConfigManager
import com.dn.sdk.business.loader.AdManager
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleInterstListener
import com.donews.common.contract.LoginHelp
import com.donews.common.contract.UserInfoBean
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

    private var mResume = false

    private val runnable = Runnable {
        showAd()
    }

    private fun resumeCheckShowAd() {
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        //注册时间大于设置时间
        if (checkUserRegisterTime(jddAdConfigBean.interstitialStartTime)) {
            //显示次数大于设置的页面最小显示次数,比如第二次显示页面才显示广告
            val resumeNumber = PageCount.getResumeNumber(mTag)
            if (resumeNumber >= jddAdConfigBean.pageShowTimes) {
                showAd()
            } else {
                val showAdNumber = PageCount.getAdShowNumber(mTag)
                if (showAdNumber < jddAdConfigBean.pageInterstitialShowTimes) {
                    sendMessage()
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


    var mActivity: AppCompatActivity? = null
    var mFragment: Fragment? = null

    fun attach(activity: AppCompatActivity) {
        mActivity = activity
        mActivity?.run {
            lifecycle.addObserver(this@PageMonitor)
            inject(this)
        }
        mActivity?.lifecycle?.addObserver(this)
    }

    fun attach(fragment: Fragment) {
        mFragment = fragment
        mFragment?.run {
            mTag = this::class.java.simpleName
            lifecycle.addObserver(this@PageMonitor)
            activity?.let {
                inject(it)
            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mResume = true
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

    /** 如果没有登录，用第一次打开app时间进行判断,否则使用注册时间进行判断 */
    private fun checkUserRegisterTime(time: Int): Boolean {
        val duration = time * 24 * 60 * 60 * 1000L;
        if (!LoginHelp.getInstance().isLogin) {
            val installApp = AppStatusUtils.getAppInstallTime();
            if (installApp - System.currentTimeMillis() >= duration) {
                return true
            }
            return false
        } else {
            return try {
                val bean = LoginHelp.getInstance().userInfoBean
                val createAt = bean.createdAt
                val registerDate = mDataFormat.parse(createAt)
                registerDate?.let {
                    return (it.time - System.currentTimeMillis()) >= duration
                } ?: kotlin.run { false }
            } catch (e: Exception) {
                val installApp = AppStatusUtils.getAppInstallTime();
                if (installApp - System.currentTimeMillis() >= duration) {
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