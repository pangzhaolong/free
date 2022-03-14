package com.dn.events.events

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 *  用户登录的状态通知
 */
class EnterShowDialogEvent(
        /**
         * 翻倍领取事件:
         *  1: 今日热门商品推荐关闭
         *  2: 再参与x次抽奖，领取下个红包->关闭
         */
        val event: Int,
)