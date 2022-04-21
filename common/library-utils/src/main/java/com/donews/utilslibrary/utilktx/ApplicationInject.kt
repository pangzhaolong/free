package com.donews.utilslibrary.utilktx

import android.app.Application

/**
 * 全局的application对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 10:55
 */
object ApplicationInject {

    private var mApplication: Application? = null

    fun init(application: Application) {
        mApplication = application
    }

    fun getApplication(): Application {
        requireNotNull(mApplication) { "application is null,please call init() method" }
        return mApplication!!
    }
}