package com.donews.unboxing.smartrefreshlayout.interfaces.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.donews.common.ext.copy
import com.donews.network.result.LoadResult
import com.donews.unboxing.smartrefreshlayout.SmartRefreshState
import com.donews.unboxing.smartrefreshlayout.interfaces.ListPagingInterface

/**
 * ListPagingInterface 实现，其余只需要实现基本方法就好
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/22 10:22
 */
abstract class AbsListPagingInterface<R, T> : ListPagingInterface<R, T> {

    companion object {
        const val PAGE_NUMBER_START = 1
    }

    final override val pageNumber: MutableLiveData<Int> = MutableLiveData()

    /** 列表请求返回数据 */
    private val loadResultData: LiveData<LoadResult<R>> = pageNumber.switchMap { pageNum ->
        getDataByPageNumber.invoke(pageNum)
    }

    override val listData: LiveData<List<T>> = loadResultData.switchMap { result ->
        disposeListResult(result)
    }
    override val refreshing: MutableLiveData<SmartRefreshState> = MutableLiveData()

    override val loadMore: MutableLiveData<SmartRefreshState> = MutableLiveData()

    override val onRefresh: () -> Unit = {
        pageNumber.value = PAGE_NUMBER_START
    }

    override val onLoadMore: () -> Unit = {
        var value = pageNumber.value ?: kotlin.run {
            PAGE_NUMBER_START
        }
        //代表上一下加载没有失败，则该次还是该请求当前页
        if (loadResultData.value !is LoadResult.Error) {
            value += 1
        }
        pageNumber.value = value
    }

    /** 处理返回数据，并返回全部数据*/
    private fun disposeListResult(result: LoadResult<R>): LiveData<List<T>> {
        val liveData = MutableLiveData<List<T>>()
        val refresh = pageNumber.value == PAGE_NUMBER_START
        val smartControl = if (refresh) refreshing else loadMore
        when (result) {
            is LoadResult.Success -> {
                liveData.value = listData.value.copy(transformData(result.data), refresh)
                val noMore = checkNoMore(result.data)
                smartControl.value = SmartRefreshState(loading = false, success = true, noMore = noMore)
            }
            is LoadResult.Error -> {
                liveData.value = listData.value.orEmpty()
                smartControl.value = SmartRefreshState(loading = false, success = false)
            }
            LoadResult.Loading -> {

            }
        }
        return liveData
    }

    /** 转化数据对象 */
    abstract fun transformData(r: R?): List<T>

    /** 通过返回数据判断是否还有更多 */
    abstract fun checkNoMore(r: R?): Boolean
}