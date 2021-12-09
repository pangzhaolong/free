package com.dn.events.events

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 *  用户登录的状态通知
 */
class DoubleRpEvent(
        /**
         * 翻倍领取事件:
         *  1:翻倍领取-完整观看视频
         *  2:v1.3.6新版翻倍领取
         *  3: 开启日历提醒
         */
        val event: Int,
        val score: Float,
        val restId: String,
        val preId: String,
)