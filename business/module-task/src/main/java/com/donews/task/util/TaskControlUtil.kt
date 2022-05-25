package com.donews.task.util

import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.task.BuildConfig
import com.donews.task.bean.TaskCenterInfo
import com.donews.task.bean.TaskConfigInfo
import com.google.gson.Gson
import com.orhanobut.logger.Logger

/**
 *  make in st
 *  on 2022/5/11 09:26
 */
object TaskControlUtil {

    //https://monetization.dev.tagtic.cn/rule/v1/calculate/free-activityConfig-dev
    private const val TASK_CONFIG = "-activityConfig"
    private const val TASK_CONFIG_URL =
        BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + TASK_CONFIG + BuildConfig.BASE_RULE_URL

    fun init(){
        getCenterConfig()
    }

    private const val taskJson = "{\"activeExchange\":{\"active\":1,\"coin\":1000},\"items\":[{\"desc\":null,\"id\":0,\"open\":true,\"repeat\":10,\"task_type\":\"turntable\",\"times\":1,\"title\":\"转盘\"},{\"desc\":null,\"id\":1,\"open\":true,\"repeat\":100,\"task_type\":\"sign\",\"times\":1,\"title\":\"签到\"},{\"desc\":null,\"id\":2,\"open\":true,\"repeat\":10,\"task_type\":\"lottery\",\"times\":1,\"title\":\"抽奖\"},{\"desc\":null,\"id\":3,\"open\":true,\"repeat\":10,\"task_type\":\"share\",\"times\":1,\"title\":\"分享\"},{\"desc\":null,\"id\":4,\"open\":true,\"repeat\":10,\"task_type\":\"collect\",\"times\":1,\"title\":\"集卡\"},{\"cd\":30,\"desc\":null,\"id\":5,\"open\":true,\"repeat\":30,\"task_type\":\"video\",\"times\":1,\"title\":\"视频\"},{\"cd\":90,\"desc\":null,\"id\":6,\"open\":true,\"repeat\":20,\"task_type\":\"giftbox\",\"times\":1,\"title\":\"宝箱\"}]}"
    /**
     * 连对奖励配置
     */
    private var taskControlConfig: TaskCenterInfo? = Gson().fromJson(taskJson,TaskCenterInfo::class.java)

    private fun getCenterConfig(){
        EasyHttp.get(TASK_CONFIG_URL)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<TaskCenterInfo>() {
                override fun onError(e: ApiException?) {
                    Logger.e(e, "")
                }

                override fun onSuccess(t: TaskCenterInfo?) {
                    t?.let {
                        taskControlConfig = it
                    }
                }
            })
    }

    fun getTaskControlConfig() = taskControlConfig

}