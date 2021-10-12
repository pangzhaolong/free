package com.donews.detail

import com.donews.base.base.BaseApplication
import com.donews.network.EasyHttp

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/12 17:00
 */
class TestApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        EasyHttp.init(this)
    }
}