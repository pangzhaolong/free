package com.dn.sdk.listener.impl

import android.view.View
import com.dn.sdk.listener.IAdNativeTemplateListener

/**
 * IAdNativeTemplateListener 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:56
 */
open class SimpleNativeTemplateListener : IAdNativeTemplateListener {
    override fun onAdStatus(code: Int, any: Any?) {

    }

    override fun onAdLoad(views: MutableList<View>) {

    }


    override fun onAdShow() {

    }

    override fun onAdExposure() {

    }

    override fun onAdClicked() {

    }

    override fun onAdClose() {

    }

    override fun onAdError(code: Int, errorMsg: String?) {

    }

    override fun onAdStartLoad() {

    }
}