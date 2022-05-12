package com.donews.middle.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/19
 * Description:
 * 支持的上拉加载更多和下拉刷新的Adapter
 */
public abstract class BaseBindingAdapter<T, B extends ViewDataBinding>
        extends BaseQuickAdapter<T, BaseBindingAdapter.BaseBindViewHolder<B>> {

    protected int layoutResId = 0;

    public BaseBindingAdapter(int layoutResId) {
        super(layoutResId);
        this.layoutResId = layoutResId;
    }

    public BaseBindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        this.layoutResId = layoutResId;
    }

    @NonNull
    @Override
    public BaseBindViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
        BaseBindViewHolder<B> vm = new BaseBindViewHolder<B>(binding.getRoot(), binding);
        return vm;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    //支持binding的类容
    public static class BaseBindViewHolder<B> extends BaseViewHolder {
        public B binding;

        public BaseBindViewHolder(@NonNull View view, B binding) {
            super(view);
            this.binding = binding;
        }
    }
}
