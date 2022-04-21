package com.donews.yfsdk.moniter

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
import com.dn.sdk.AdCustomError
import com.dn.sdk.utils.AdLoggerUtils
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
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

    private val mBlackList = arrayListOf<String>()

    private var mResume = false

    private lateinit var mPageListener: PageListener

    private val runnable = Runnable {
        showAd()
    }


    var mActivity: AppCompatActivity? = null
    var mFragment: Fragment? = null

    fun attach(activity: AppCompatActivity, tag: String = activity.javaClass.simpleName, listener: PageListener) {
        mPageListener = listener
        with(activity) {
            mTag = tag
            if (!mBlackList.contains(mTag)) {
                lifecycle.addObserver(this@PageMonitor)
                inject(this)
                mActivity = this
            }
        }
    }

    fun attach(activity: AppCompatActivity, listener: PageListener) {
        attach(activity, activity.javaClass.simpleName, listener)
    }

    fun attach(fragment: Fragment, tag: String = fragment.javaClass.simpleName, listener: PageListener) {
        mPageListener = listener
        with(fragment) {
            mTag = tag
            if (!mBlackList.contains(mTag)) {
                lifecycle.addObserver(this@PageMonitor)
                activity?.let {
                    inject(it)
                }
                mFragment = this
            }
        }
    }

    fun attach(fragment: Fragment, listener: PageListener) {
        attach(fragment, fragment.javaClass.simpleName, listener)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mResume = true
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
        mHandler.postDelayed(runnable, mPageListener.getIdleTime() * 1000L)
    }

    private fun detach() {
        mActivity?.lifecycle?.removeObserver(this)
        mFragment?.lifecycle?.removeObserver(this)
    }

    private fun resumeCheckShowAd() {
        val adCheckResult = mPageListener.checkShowAd()
        if (adCheckResult == AdCustomError.OK) {
            showAd()
        } else {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            sendMessage()
        }
    }


    private fun showAd() {
        //显示次数小于最大显示次数
        val activity = mActivity ?: mFragment?.activity
        if (activity != null && !activity.isFinishing) {
            val adCheckResult = mPageListener.checkShowAd()
            if (adCheckResult == AdCustomError.OK) {
                mPageListener.showAd()
            } else {
                AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            }
        }
        sendMessage()
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
                        sendMessage()
                    }
                }
            }
            return method?.invoke(callback, *(args ?: arrayOfNulls<Any>(0)))
        }
    }

    interface PageListener {
        fun getIdleTime(): Int
        fun showAd()
        fun checkShowAd(): AdCustomError
    }
}