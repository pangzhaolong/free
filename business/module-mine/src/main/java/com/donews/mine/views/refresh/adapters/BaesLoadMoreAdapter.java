package com.donews.mine.views.refresh.adapters;

import static java.lang.Math.ceil;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.listener.OnUpFetchListener;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/19
 * Description:
 * 支持的上拉加载更多和下拉刷新的Adapter
 */
public abstract class BaesLoadMoreAdapter<T, VH extends BaseViewHolder>
        extends BaseQuickAdapter<T, VH> implements LoadMoreModule {

    /**
     * 上拉加载更多的监听
     */
    public interface BaseLoadMoreListener {
        /**
         * 触发了上拉加载更多
         *
         * @param page     需要加载的页码(从 1 开始)
         * @param pageSize 每页数据量的大小
         */
        void loadMore(int page, int pageSize);
    }

    /**
     * 页码大小
     */
    private int pageSize = 25;

    /**
     * 上拉加载更多
     */
    private BaseLoadMoreListener loadMoreListener;

    public BaesLoadMoreAdapter(int layoutResId) {
        super(layoutResId);
        init();
    }

    public BaesLoadMoreAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        init();
    }

    /**
     * 上拉加载更多完成
     *
     * @param isSucc 是否成功
     * @param isLoadEnd 是否结束自动加载，一般再在无数据返回时候结束
     *                  T:结束加载，F:允许加载
     */
    public void loadMoreFinish(Boolean isSucc,Boolean isLoadEnd) {
        if(isSucc) {
            getLoadMoreModule().loadMoreComplete();
            if(isLoadEnd){
                //结束加载更多
                getLoadMoreModule().loadMoreEnd();
            }
        }else{
            getLoadMoreModule().loadMoreFail();
        }
    }

    /**
     * 获取需要加载更多时候的页码，加载的数据时候需要的页码
     *
     * @return 分页加载的页码
     */
    public int getLoadMorePage() {
        return (int) ceil(getData().size() / (pageSize * 1.0));
    }

    /**
     * 设置上拉加载更多
     *
     * @param listener
     */
    public void setOnLoadMoreListener(BaseLoadMoreListener listener) {
        loadMoreListener = listener;
        getLoadMoreModule().setEnableLoadMore(true);
        getLoadMoreModule().setAutoLoadMore(true);
        getLoadMoreModule().setOnLoadMoreListener(() -> {
            loadMoreListener.loadMore(getLoadMorePage(), pageSize);
        });
    }

    private void init() {
    }
}
