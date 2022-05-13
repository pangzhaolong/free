package com.donews.task.vm

import com.donews.base.model.BaseLiveDataModel
import com.donews.middle.BuildConfig
import com.donews.middle.mainShare.upJson.PostBean
import com.donews.middle.mainShare.upJson.PostExchangeBean
import com.donews.middle.mainShare.upJson.PostReportBean
import com.donews.middle.mainShare.upJson.PostStringBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.task.bean.BubbleReceiveInfo
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 *  make in st
 *  on 2022/5/7 10:40
 */
class TaskRepository : BaseLiveDataModel() {

    /**
     * 气泡点击领取
     * notice: mId是100表示一键领取所有气泡,只需传mType为none
     */
    fun bubbleReceive(mId:Int,mType:String): Flow<BubbleReceiveInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/activity-tasks-receive")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(if (mId == 100){
                    Gson().toJson(PostStringBean(mType))
                } else Gson().toJson(PostBean(mId,mType)))
                .execute(object : SimpleCallBack<BubbleReceiveInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: BubbleReceiveInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 活跃度兑换金币
     */
    fun exchange(exchangeActiveNum:Int): Flow<Any?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/activity-exchange")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostExchangeBean(exchangeActiveNum)))
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