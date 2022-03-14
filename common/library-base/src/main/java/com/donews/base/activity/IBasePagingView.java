package com.donews.base.activity;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * 作者： created by honeylife<br>
 * 日期：2020-01-27
 */
public interface IBasePagingView extends IBaseView {
    /**
     * 加载更多失败
     * */
    void onLoadMoreFailure(String message);

    /**
     * 没有更多了
     * */
    void onLoadMoreEmpty();
}
