package com.donews.detail

import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.base.BaseApplication
import com.donews.network.EasyHttp
import com.donews.utilslibrary.utils.LogUtil

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
        if (LogUtil.allow) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }
}