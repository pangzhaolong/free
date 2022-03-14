package com.dn.events

import android.app.Application
import com.blankj.utilcode.util.NetworkUtils
import com.dn.events.events.NetworkChanageEvnet
import org.greenrobot.eventbus.EventBus

/**
 * @author lcl
 * Date on 2021/10/8
 * Description:
 */
object DNEventBusUtils {
    internal lateinit var app: Application

    /**
     * 初始化框架
     * @param app Application
     */
    fun init(app: Application) {
        this.app = app
        //注册网络状态监听
        NetworkUtils.registerNetworkStatusChangedListener(object :
            NetworkUtils.OnNetworkStatusChangedListener {
            override fun onDisconnected() {
                EventBus.getDefault().post(NetworkChanageEvnet(-1))
            }

            override fun onConnected(networkType: NetworkUtils.NetworkType) {
                when (networkType) {
                    NetworkUtils.NetworkType.NETWORK_5G,
                    NetworkUtils.NetworkType.NETWORK_4G,
                    NetworkUtils.NetworkType.NETWORK_3G,
                    NetworkUtils.NetworkType.NETWORK_2G -> {
                        EventBus.getDefault().post(NetworkChanageEvnet(0))
                    }
                    NetworkUtils.NetworkType.NETWORK_WIFI -> {
                        EventBus.getDefault().post(NetworkChanageEvnet(1))
                    }
                    else -> {
                        EventBus.getDefault().post(NetworkChanageEvnet(2))
                    }
                }
            }
        })
    }
}