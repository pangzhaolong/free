package com.dn.events.events

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 *  用户登录的状态通知
 */
class RedPackageStatus(
        /**
         * 抽奖次数:
         *   0:刷新主页面浮窗红包显示
         */
        val status: Int,
        val counts: Int,
)