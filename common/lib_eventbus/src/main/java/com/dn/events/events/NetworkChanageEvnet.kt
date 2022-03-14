package com.dn.events.events

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 * 网络变化的状态通知事件
 */
class NetworkChanageEvnet(
    /**
     * -1：网络断开连接
     *  0：数据流量(5g、4g、3g、2g)
     *  1：wifi流量
     *  2:未知网络
     */
    val type:Int
)