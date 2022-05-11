package com.donews.task.vm

import com.donews.base.model.BaseLiveDataModel
import com.donews.middle.BuildConfig
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.middle.mainShare.upJson.PostBean
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
     */
    fun bubbleReceive(mId:Int,mType:String): Flow<BubbleReceiveInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/activity-tasks-receive")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostBean(mId,mType)))
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

}