package com.donews.middle.mainShare.repository

import com.donews.base.model.BaseLiveDataModel
import com.donews.middle.BuildConfig
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
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

}