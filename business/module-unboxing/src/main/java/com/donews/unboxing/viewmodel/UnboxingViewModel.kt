package com.donews.unboxing.viewmodel

import androidx.lifecycle.MutableLiveData
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.repository.UnboxingRepository

/**
 *  晒单页 ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 17:47
 */
class UnboxingViewModel : BaseLiveDataViewModel<UnboxingRepository>() {

    companion object {
        const val PAGE_SIZE = 20
    }

    private val allLoadData = mutableListOf<UnboxingBean>()

    private val _unboxingLiveData = MutableLiveData<MutableList<UnboxingBean>>()
    val unboxingLiveData = _unboxingLiveData

    var mPageId = 0

    override fun createModel(): UnboxingRepository {
        return UnboxingRepository()
    }


    fun getUnboxingData() {
        mPageId = 0
        mModel.getUnboxingData(mPageId, PAGE_SIZE, object : SimpleCallBack<MutableList<UnboxingBean>>() {
            override fun onError(e: ApiException) {

            }

            override fun onSuccess(t: MutableList<UnboxingBean>?) {
                t?.let {
                    allLoadData.clear()
                    allLoadData.addAll(it)
                    _unboxingLiveData.value = it
                    mPageId++
                }
            }
        })
    }

    fun loadMoreData() {
        mModel.getUnboxingData(mPageId, PAGE_SIZE, object : SimpleCallBack<MutableList<UnboxingBean>>() {
            override fun onError(e: ApiException) {

            }

            override fun onSuccess(t: MutableList<UnboxingBean>?) {
                t?.let {
                    allLoadData.addAll(it)
                    _unboxingLiveData.value = it
                    mPageId++
                }
            }
        })
    }

}