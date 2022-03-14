package com.dn.events.events;

class FrontScrollEvent(
        /**
         * 首页滚动是否显示 回到顶部按钮
         * status:0 隐藏 ; 1 显示
         */

        val event: Int,
        val status: Int
)