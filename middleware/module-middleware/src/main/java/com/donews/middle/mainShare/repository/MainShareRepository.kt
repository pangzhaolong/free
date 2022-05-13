package com.donews.middle.mainShare.repository

import com.donews.base.model.BaseLiveDataModel
import com.donews.middle.BuildConfig
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.middle.mainShare.bean.TaskBubbleInfo
import com.donews.middle.mainShare.upJson.PostReportBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 *  make in st
 *  on 2022/5/11 11:31
 *  Main公共数据分享
 */
class MainShareRepository: BaseLiveDataModel() {

    /**
     * 获取用户金币和活跃度
     */
    fun getUserAssets(): Flow<UserAssetsResp?> {
        return callbackFlow {
            val disposable = EasyHttp.get(BuildConfig.BASE_QBN_API + "activity/v1/user-assets")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<UserAssetsResp>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: UserAssetsResp?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 获取任务气泡列表
     */
    fun getTaskBubbles(): Flow<TaskBubbleInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.get(BuildConfig.BASE_QBN_API + "activity/v1/activity-tasks")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<TaskBubbleInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: TaskBubbleInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 看广告上报,集卡,分享,抽奖都要上报,签到和转盘无需上报
     */
    fun adReport(mId:Int,mType:String): Flow<Any?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/report")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostReportBean(mId,mType,(System.currentTimeMillis() / 1000).toString())))
                .execute(object : SimpleCallBack<Any>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: Any?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

}