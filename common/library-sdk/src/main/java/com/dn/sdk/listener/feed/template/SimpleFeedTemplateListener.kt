package com.dn.sdk.listener.feed.template

import android.view.View

/**
 * IAdNativeTemplateListener 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:56
 */
open class SimpleFeedTemplateListener : IAdFeedTemplateListener {
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