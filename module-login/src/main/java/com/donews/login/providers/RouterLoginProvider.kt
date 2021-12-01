package com.donews.login.providers

import android.annotation.SuppressLint
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.events.events.LoginLodingStartStatus
import com.donews.common.router.RouterActivityPath.LoginProvider.PROVIDER_LOGIN
import com.donews.common.router.providers.IARouterLoginProvider
import com.donews.login.model.UserInfoManage
import com.donews.login.providers.RouterLoginProvider.Companion.context
import com.donews.share.ISWXSuccessCallBack
import com.donews.share.WXHolderHelp
import com.donews.utilslibrary.analysis.AnalysisUtils
import com.donews.utilslibrary.dot.Dot
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.LogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author lcl
 * Date on 2021/11/12
 * Description:
 * ARouter中提供登录的提供者实现
 */
@Route(path = PROVIDER_LOGIN, name = "登录模块中的登录功能提供者")
class RouterLoginProvider : IARouterLoginProvider {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    override fun init(context: Context) {
        RouterLoginProvider.context = context
    }

    override fun loginWX(loginTag: String?) {
        loginWX(loginTag, null)
    }

    override fun loginWX(loginTag: String?, from: String?) {
        WXHolderHelp.login(object : ISWXSuccessCallBack {
            override fun onSuccess(state: Int, wxCode: String?) {
                LogUtil.i("state和code的值$state==$wxCode")
                AppInfo.saveWXLoginCode(wxCode)
                UserInfoManage.onLoadNetUserInfo(
                    UserInfoManage.getNetDataStr(wxCode),
                    loginTag,
                    from
                )
            }

            override fun onFailed(msg: String?) {
                if (from?.isNotEmpty() == true && context != null) {
                    AnalysisUtils.onEventEx(
                        context,
                        Dot.WX_Login, "$from(微信服务异常)"
                    )
                }
            }
        })
    }
}