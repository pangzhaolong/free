package com.module.lottery.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, V extends ViewDataBinding> extends RecyclerView.Adapter<T> {
    public abstract int getLayout();
    public V mDataBinding;
    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayout(), parent, false);
        T viewHolder = getViewHolder(mDataBinding);
        return viewHolder;
    }


    public abstract T getViewHolder(V viewDataBinding);




}
