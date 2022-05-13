package com.donews.task.util

import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.task.BuildConfig
import com.donews.task.bean.TaskConfigInfo
import com.google.gson.Gson
import com.orhanobut.logger.Logger

/**
 *  make in st
 *  on 2022/5/11 09:26
 */
object TaskControlUtil {

    //https://monetization.dev.tagtic.cn/rule/v1/calculate/free-taskConfig-dev
    private const val TASK_CONFIG = "-taskConfig"
    private const val TASK_CONFIG_URL =
        BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + TASK_CONFIG + BuildConfig.BASE_RULE_URL

    fun init(){
        getCenterConfig()
    }

    private const val taskJson = "{\"ad\":{\"mMaxCountTime\":180,\"todaySeeAdMaxNum\":3},\"box\":{\"boxMaxOpenNum\":5,\"boxMaxTime\":120},\"exchange\":{\"exchangeActiveNum\":15},\"img\":{\"luckCollectImg\":\"https://img0.baidu.com/it/u=3529581707,981182388\\u0026fm=253\\u0026fmt=auto\\u0026app=138\\u0026f=JPEG?w=500\\u0026h=202\",\"luckPanImg\":\"https://img0.baidu.com/it/u=3529581707,981182388\\u0026fm=253\\u0026fmt=auto\\u0026app=138\\u0026f=JPEG?w=500\\u0026h=202\"}}"
    /**
     * 连对奖励配置
     */
    private var taskControlConfig: TaskConfigInfo? = Gson().fromJson(taskJson,TaskConfigInfo::class.java)

    private fun getCenterConfig(){
        EasyHttp.get(TASK_CONFIG_URL)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<TaskConfigInfo>() {
                override fun onError(e: ApiException?) {
                    Logger.e(e, "")
                }

                override fun onSuccess(t: TaskConfigInfo?) {
                    t?.let {
                        taskControlConfig = it
                    }
                }
            })
    }

    fun getTaskControlConfig() = taskControlConfig

}