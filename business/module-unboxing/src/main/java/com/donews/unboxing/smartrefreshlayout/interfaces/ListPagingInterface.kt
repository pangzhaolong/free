package com.donews.unboxing.smartrefreshlayout.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.donews.network.result.LoadResult
import com.donews.unboxing.bean.UnBoxingResp
import com.donews.unboxing.smartrefreshlayout.SmartRefreshState

/**
 * 分页请求处理接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/21 17:48
 */
interface ListPagingInterface<R, T> {

    /** 页面 */
    val pageNumber: MutableLiveData<Int>

    /** 列表数据 */
    val listData: LiveData<List<T>>

    /** 刷新状态 */
    val refreshing: MutableLiveData<SmartRefreshState>

    /** 加载更多状态 */
    val loadMore: MutableLiveData<SmartRefreshState>

    /** 刷新回调 */
    val onRefresh: () -> Unit

    /** 加载更多回调 */
    val onLoadMore: () -> Unit

    /** 通过页面获取对应页数据 */
    var getDataByPageNumber: (Int) -> LiveData<LoadResult<R>>
}