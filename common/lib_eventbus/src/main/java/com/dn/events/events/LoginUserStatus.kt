package com.dn.events.events

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 *  用户登录的状态通知
 */
class LoginUserStatus(
    /**
     * 登录状态:
     *  -3:刷新token接口报错
     *  -2:刷新token接口报错
     *  -1:登录接口报错
     *   0:登录失败(接口成功,但是未能正确获得数据)
     *   1:登录成功(登录或token刷新)
     *   2:退出登录(清除了所有的登录信息)
     *   3:退出微信登录(即本地的退出登录)
     */
    val status: Int,
)