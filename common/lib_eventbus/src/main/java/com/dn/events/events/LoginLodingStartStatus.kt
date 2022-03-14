package com.dn.events.events

import androidx.lifecycle.MutableLiveData

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 *  用户开始发起登录的通知,此通知只会再发起登录时候调用一次。后续的状态更新则是需要再[loginLoadingLiveData]属性中更新
 */
class LoginLodingStartStatus {

    /**
     * 登录发起过程的监听通知(每次请求都会有一个通知对象)
     * -1:登录失败
     * 0:开始发起登录
     * 1:登录成功。但是用户信息获取失败
     * 2:登录成功并且获取用户信息成功
     */
    val loginLoadingLiveData: MutableLiveData<Int> = MutableLiveData(0)

    /**
     * tag标记。用于标识此通知属于谁。给这个通知的唯一标记
     */
    var tag: String = this.toString()

    /**
     * 是否为预注册,T:预注册。默认设备登录，F:非预注册。微信登录
     */
    var isPreReg: Boolean = false

    constructor() {
    }

    constructor(tag: String) {
        this.tag = tag
    }
}