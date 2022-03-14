package com.donews.unboxing.smartrefreshlayout.interfaces.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.network.result.LoadResult
import com.donews.unboxing.bean.UnBoxingResp
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.repository.UnboxingRepository

/**
 *  晒单页分页加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/21 17:57
 */
class UnboxingListPagingInterfaceImpl(
    private val repository: UnboxingRepository
) : AbsListPagingInterface<UnBoxingResp, UnboxingBean>() {

    companion object {
        const val PAGE_SIZE = 20
    }

    override var getDataByPageNumber: (Int) -> LiveData<LoadResult<UnBoxingResp>> = {
        val result = MutableLiveData<LoadResult<UnBoxingResp>>()
        result.value = LoadResult.Loading
        repository.getUnboxingData(it, PAGE_SIZE, object : SimpleCallBack<UnBoxingResp>() {
            override fun onError(e: ApiException) {
                result.value = LoadResult.Error(e)
            }

            override fun onSuccess(t: UnBoxingResp?) {
                t?.let { data ->
                    result.value = LoadResult.Success(data)
                }
            }
        })
        result
    }

    override fun transformData(r: UnBoxingResp?): List<UnboxingBean> {
        val data = arrayListOf<UnboxingBean>()
        r?.let {
            data.addAll(it.data)
        }
        return data
    }

    override fun checkNoMore(r: UnBoxingResp?): Boolean {
        var result = false
        if (r == null) {
            return result
        }
        result = r.data.size < PAGE_SIZE
        return result
    }
}