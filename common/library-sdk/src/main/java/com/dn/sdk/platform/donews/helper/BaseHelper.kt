package com.dn.sdk.platform.donews.helper

import android.app.Activity
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.donews.ads.mediation.v2.api.DoNewsAdNative

/**
 * 基础加载广告Helper
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 15:23
 */
open class BaseHelper {

    /** 将广告对象和activity生命周期绑定，防止发送泄漏 */
    fun bindLifecycle(activity: Activity?, doNewsAdNative: DoNewsAdNative?, customDestroy: (() -> Unit)? = null) {
        if (activity is AppCompatActivity && Looper.getMainLooper() == Looper.myLooper()) {
            activity.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        activity.lifecycle.removeObserver(this)
                        customDestroy?.invoke() ?: kotlin.run {
                            doNewsAdNative?.destroy()
                        }
                    }
                }
            })
        }
    }

    fun runOnUiThread(activity: Activity, runnable: () -> Unit) {
        activity.runOnUiThread {
            runnable.invoke()
        }
    }
}