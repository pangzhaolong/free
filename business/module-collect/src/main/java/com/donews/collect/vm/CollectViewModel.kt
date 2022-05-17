package com.donews.collect.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.collect.bean.DanMuInfo
import com.donews.collect.bean.GoodInfo
import com.donews.collect.bean.StatusInfo
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
    private val mNewGoodCard: MutableLiveData<Any> = MutableLiveData()
    val newGoodCard: LiveData<Any> = mNewGoodCard

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

}