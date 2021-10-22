package com.donews.unboxing.smartrefreshlayout

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 *  SmartRefreshLayout dataBing 双向绑定
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/21 16:47
 */

/** 扩展属性，是否正在刷新 */
@set:BindingAdapter("binding_smart_refreshing")
@get:InverseBindingAdapter(attribute = "bind_smart_refreshing", event = "bind_smart_refreshingAttrChanged")
var SmartRefreshLayout.refreshing: Boolean
    get() = state == RefreshState.Refreshing
    set(value) {
        if (refreshing == value) {
            return
        }
        if (value) {
            autoRefresh()
        } else {
            finishRefresh()
        }
    }

/** 扩展属性，是否正在加载更多 */
@set:BindingAdapter("binding_smart_loadMore")
@get:InverseBindingAdapter(attribute = "bind_smart_loadMore", event = "bind_smart_loadMoreAttrChanged")
var SmartRefreshLayout.loadMore: Boolean
    get() = state == RefreshState.Loading
    set(value) {
        if (loadMore == value) {
            return
        }
        if (value) {
            autoLoadMore()
        } else {
            finishLoadMore()
        }
    }

/** 扩展属性 刷新状态，包含是否正在刷新、是否成功、结束延时、是否没有更多 */
@set:BindingAdapter("binding_smart_refreshState")
@get:InverseBindingAdapter(attribute = "binding_smart_refreshState", event = "bind_smart_refreshingAttrChanged")
var SmartRefreshLayout.refreshState: SmartRefreshState?
    get() = SmartRefreshState(refreshing)
    set(value) {
        if (value == null) {
            return
        }
        if (refreshing == value.loading) {
            return
        }
        if (value.loading) {
            if (value.delay == 0) {
                autoRefresh()
            } else {
                autoRefresh(value.delay)
            }
        } else {
            //加载结束
            // 刷新结束
            if (value.delay == 0 && !value.noMore) {
                finishRefresh(value.success)
            } else if (value.delay > 0 && value.success && !value.noMore) {
                finishRefresh(value.delay)
            } else {
                finishRefresh(value.delay, value.success, value.noMore)
            }
        }
    }

/** 扩展属性 加载更多状态，包含是否正在加载、是否成功、结束延时、是否没有更多 */
@set:BindingAdapter("binding_smart_loadMoreState")
@get:InverseBindingAdapter(attribute = "binding_smart_loadMoreState", event = "bind_smart_loadMoreAttrChanged")
var SmartRefreshLayout.loadMoreState: SmartRefreshState?
    get() = SmartRefreshState(loadMore)
    set(value) {
        if (value == null) {
            return
        }
        if (loadMore == value.loading) {
            return
        }
        if (value.loading) {
            if (value.delay == 0) {
                autoLoadMore()
            } else {
                autoLoadMore(value.delay)
            }
        } else {
            //加载结束
            if (value.delay == 0 && !value.noMore) {
                finishLoadMore(value.success)
            } else if (value.delay > 0 && value.success && !value.noMore) {
                finishLoadMore(value.delay)
            } else {
                finishLoadMore(value.delay, value.success, value.noMore)
            }
        }
    }


/**
 * 给 [srl] 设置是否允许下拉刷新 [enable]
 */
@BindingAdapter("binding_smart_refresh_enable")
fun setSmartRefreshLayoutRefreshEnable(srl: SmartRefreshLayout, enable: Boolean) {
    srl.setEnableRefresh(enable)
}

/**
 * 给 [srl] 设置刷新回调 [onRefresh]
 * > [onRefresh] 无入参，无返回值
 *
 * > [listener] 为属性变化监听，`DataBinding` 自动实现，无需配置
 */
@BindingAdapter("binding_smart_onRefresh", "bind_smart_refreshingAttrChanged", requireAll = false)
fun setSmartRefreshLayoutOnRefresh(
    srl: SmartRefreshLayout,
    onRefresh: (() -> Unit)?,
    inverseBindingListener: InverseBindingListener
) {
    srl.setOnRefreshListener {
        inverseBindingListener.onChange()
        onRefresh?.invoke()
    }
}


/**
 * 给 [srl] 设置是否允许下加载更多[enable]
 */
@BindingAdapter("binding_smart_loadMore_enable")
fun setSmartRefreshLayoutLoadMoreEnable(srl: SmartRefreshLayout, enable: Boolean) {
    srl.setEnableLoadMore(enable)
}

/**
 * 给 [srl] 设置加载更多回调 [onLoadMore]
 * > [onLoadMore] 无入参，无返回值
 *
 * > [inverseBindingListener] 为属性变化监听，`DataBinding` 自动实现，无需配置
 */
@BindingAdapter("binding_smart_onLoadMore", "bind_smart_loadMoreAttrChanged", requireAll = false)
fun setSmartRefreshLayoutOnLoadMore(
    srl: SmartRefreshLayout,
    onLoadMore: (() -> Unit)?,
    inverseBindingListener: InverseBindingListener
) {

    srl.setOnLoadMoreListener {
        inverseBindingListener.onChange()
        onLoadMore?.invoke()
    }
}


data class SmartRefreshState(
    val loading: Boolean,
    val success: Boolean = true,
    val noMore: Boolean = false,
    val delay: Int = 0
)