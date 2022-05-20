package com.donews.collect.vm

import com.donews.base.model.BaseLiveDataModel
import com.donews.collect.bean.*
import com.donews.middle.BuildConfig
import com.donews.middle.mainShare.upJson.PostCardBean
import com.donews.middle.mainShare.upJson.PostGoodBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 *  make in st
 *  on 2022/5/16 09:56
 */
class CollectRepository: BaseLiveDataModel() {

    /**
     * 气泡点击领取
     * notice: mId是100表示一键领取所有气泡,只需传mType为none
     */
    fun getDanMu(): Flow<DanMuInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.get(BuildConfig.BASE_QBN_API + "activity/v1/card-barrage")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<DanMuInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: DanMuInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 集卡状态获取
     * notice: 集卡状态 0 未集卡 1 集卡中 2 集卡完成 3 集卡失败
     */
    fun getStatus(): Flow<StatusInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.get(BuildConfig.BASE_QBN_API + "activity/v1/card-status")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<StatusInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: StatusInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 集卡商品列表
     */
    fun getGoods(): Flow<GoodInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.get(BuildConfig.BASE_QBN_API + "activity/v1/card-goods")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<GoodInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: GoodInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 选择新的福利卡
     */
    fun startNewCard(goodId: String): Flow<StatusInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/start-card")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostGoodBean(goodId)))
                .execute(object : SimpleCallBack<StatusInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: StatusInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 结束当前集卡
     */
    fun startStopCard(cardId: String): Flow<StatusInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/stop-card")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostCardBean(cardId)))
                .execute(object : SimpleCallBack<StatusInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: StatusInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 抽碎片
     */
    fun startDrawCard(cardId: String): Flow<DrawCardInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/draw-card")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostCardBean(cardId)))
                .execute(object : SimpleCallBack<DrawCardInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: DrawCardInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

    /**
     * 充能
     */
    fun startCardCharge(cardId: String): Flow<CardChargeInfo?> {
        return callbackFlow {
            val disposable = EasyHttp.post(BuildConfig.BASE_QBN_API + "activity/v1/card-charge")
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(Gson().toJson(PostCardBean(cardId)))
                .execute(object : SimpleCallBack<CardChargeInfo>() {
                    override fun onError(e: ApiException?) {
                        trySend(null)
                    }

                    override fun onSuccess(t: CardChargeInfo?) {
                        trySend(t)
                    }
                })
            awaitClose {
                disposable.dispose()
            }
        }
    }

}