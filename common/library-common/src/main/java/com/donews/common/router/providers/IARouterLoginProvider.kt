package com.donews.common.router.providers

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author lcl
 * Date on 2021/11/12
 * Description:
 * 提供登录功能的提供者。
 */
interface IARouterLoginProvider : IProvider {

    /**
     * 直接调用微信登录,登录结果监听通过EventBus通知
     *  [com.dn.events.events.LoginUserStatus] :用户登录结果的通知
     *  [com.dn.events.events.LoginLodingStartStatus] :登录过程的监听。可以设置标记作为自己关心的通知处理
     * @param loginTag 本次登录的标记，用于唯一标记本次登录的标记
     */
    fun loginWX(loginTag:String? = null)

    /**
     * 直接调用微信登录,登录结果监听通过EventBus通知
     *  [com.dn.events.events.LoginUserStatus] :用户登录结果的通知
     *  [com.dn.events.events.LoginLodingStartStatus] :登录过程的监听。可以设置标记作为自己关心的通知处理
     * @param loginTag 本次登录的标记，用于唯一标记本次登录的标记
     * @param from 登录来源(用于给后台上报登录来源的tag)，如果为空表示不上报。直接调用:[loginWX]方法即可
     */
    fun loginWX(loginTag:String? = null,from:String? = null)

}