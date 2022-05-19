package com.donews.collect.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.collect.bean.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *  make in st
 *  on 2022/5/16 09:55
 */
class CollectViewModel: BaseLiveDataViewModel<CollectRepository>() {

    override fun createModel(): CollectRepository {
        return CollectRepository()
    }

    //弹幕
    private val mDanMu: MutableLiveData<DanMuInfo> = MutableLiveData()
    val danMu: LiveData<DanMuInfo> = mDanMu

    //弹幕
    fun requestDanMu() {
        viewModelScope.launch {
            mModel.getDanMu().collect {
                it?.let {
                    mDanMu.postValue(it)
                } ?: kotlin.run {
                    mDanMu.postValue(null)
                }
            }
        }
    }

    //集卡状态
    private val mStatus: MutableLiveData<StatusInfo> = MutableLiveData()
    val status: LiveData<StatusInfo> = mStatus

    //集卡状态
    fun requestStatus() {
        viewModelScope.launch {
            mModel.getStatus().collect {
                it?.let {
                    mStatus.postValue(it)
                } ?: kotlin.run {
                    mStatus.postValue(null)
                }
            }
        }
    }

    //集卡商品
    private val mGoodInfo: MutableLiveData<GoodInfo> = MutableLiveData()
    val goodsInfo: LiveData<GoodInfo> = mGoodInfo

    //集卡商品
    fun requestGoods() {
        viewModelScope.launch {
            mModel.getGoods().collect {
                it?.let {
                    mGoodInfo.postValue(it)
                } ?: kotlin.run {
                    mGoodInfo.postValue(null)
                }
            }
        }
    }

    //选择新的福利卡
    private val mNewGoodCard: MutableLiveData<StatusInfo> = MutableLiveData()
    val newGoodCard: LiveData<StatusInfo> = mNewGoodCard

    //集卡商品
    fun requestNewGoodCard(goodId: String) {
        viewModelScope.launch {
            mModel.startNewCard(goodId).collect {
                it?.let {
                    mNewGoodCard.postValue(it)
                } ?: kotlin.run {
                    mNewGoodCard.postValue(null)
                }
            }
        }
    }

    //结束当前集卡
    private val mStopCard: MutableLiveData<Any> = MutableLiveData()
    val stopCard: LiveData<Any> = mStopCard

    //结束当前集卡
    fun requestStopCard(goodId: String) {
        viewModelScope.launch {
            mModel.startStopCard(goodId).collect {
                it?.let {
                    mStopCard.postValue(it)
                } ?: kotlin.run {
                    mStopCard.postValue(null)
                }
            }
        }
    }

    //抽碎片
    private val mDrawCard: MutableLiveData<DrawCardInfo> = MutableLiveData()
    val drawCard: LiveData<DrawCardInfo> = mDrawCard

    //抽碎片
    fun requestDrawCard(goodId: String) {
        viewModelScope.launch {
            mModel.startDrawCard(goodId).collect {
                it?.let {
                    mDrawCard.postValue(it)
                } ?: kotlin.run {
                    mDrawCard.postValue(null)
                }
            }
        }
    }

    //充能
    private val mCardCharge: MutableLiveData<CardChargeInfo> = MutableLiveData()
    val cardCharge: LiveData<CardChargeInfo> = mCardCharge

    //充能
    fun requestCardCharge(goodId: String) {
        viewModelScope.launch {
            mModel.startCardCharge(goodId).collect {
                it?.let {
                    mCardCharge.postValue(it)
                } ?: kotlin.run {
                    mCardCharge.postValue(null)
                }
            }
        }
    }

}