package com.donews.library_recyclerview;

import android.util.SparseArray;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;


/**
 * RecyclerView的基础的ViewHolder
 */
public class DataBindBaseViewHolder extends BaseViewHolder {
    private ViewDataBinding binding;
    private View mConvertView;
    private SparseArray<View> mViews;

    public DataBindBaseViewHolder(View itemView) {
        super(itemView);
        this.binding = DataBindingUtil.bind(itemView);
        this.mConvertView = itemView;
        this.mViews = new SparseArray<>();
    }

    public ViewDataBinding getBinding() {
        return binding;
    }


    /**
     * 获取convertView
     *
     * @return
     */
    public View getConvertView() {
        return this.mConvertView;
    }

}
