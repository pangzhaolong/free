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

    private val _unboxingLiveData = MutableLiveData<MutableList<UnboxingBean>>()
    val unboxingLiveData = _unboxingLiveData

    override fun createModel(): UnboxingRepository {
        return UnboxingRepository()
    }


    fun getUnboxingData() {
        mModel.getUnboxingData(object : SimpleCallBack<MutableList<UnboxingBean>>() {
            override fun onError(e: ApiException) {

            }

            override fun onSuccess(t: MutableList<UnboxingBean>?) {
                t?.let {
                    _unboxingLiveData.value = it
                }
            }
        })
    }

}