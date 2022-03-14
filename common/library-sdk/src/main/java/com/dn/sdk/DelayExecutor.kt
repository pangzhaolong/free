package com.dn.sdk

import android.os.Handler
import android.os.Looper

/**
 *延迟执行
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/10 14:59
 */
object DelayExecutor {

    private val mDelayHandler = Handler(Looper.getMainLooper())


    fun delayExec(delayTime: Long = 1000, delayRunnable: () -> Unit) {
        mDelayHandler.postDelayed({
            delayRunnable.invoke()
        }, delayTime)
    }
}