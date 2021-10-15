package com.donews.network.result

/**
 * 网络请求过程状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/15 11:14
 */
sealed class LoadResult<out T> {
    object Loading : LoadResult<Nothing>()

    data class Success<out T>(val data: T? = null) : LoadResult<T>()

    data class Error(val throwable: Throwable) : LoadResult<Nothing>()
}
