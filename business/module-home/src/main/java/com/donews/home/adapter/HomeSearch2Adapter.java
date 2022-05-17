package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.home.R;
import com.donews.home.databinding.HomeExchanageFragmentGoodsItemBinding;
import com.donews.home.databinding.HomeSearch2ItemBinding;
import com.donews.home.databinding.HomeSearch2SearchBindingImpl;
import com.donews.middle.base.BaseBindingAdapter;
import com.donews.middle.bean.home.HomeExchangeGoodsBean;
import com.donews.middle.bean.home.SearchRespBean;

import java.util.List;

public class HomeSearch2Adapter extends
        BaseBindingAdapter<SearchRespBean.SearchRespItemBean, HomeSearch2ItemBinding> {

    public interface GoodsClickListener {
        /**
         * 对话按钮的点击
         *
         * @param item
         */
        void onClick(SearchRespBean.SearchRespItemBean item);
    }

    private final Context mContext;
    private GoodsClickListener mListener;

    public HomeSearch2Adapter(Context context, GoodsClickListener listener) {
        super(R.layout.home_search_2_item);
        this.mContext = context;
        this.mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<SearchRespBean.SearchRespItemBean> list, boolean needClear) {
        if (needClear) {
            setNewData(list);
        } else {
            addData(list);
        }
    }

    @Override
    protected void convert(@NonNull BaseBindViewHolder<HomeSearch2ItemBinding> helper,
                           @Nullable SearchRespBean.SearchRespItemBean goodsInfo) {
        helper.binding.setThiz(this);
        helper.binding.setGoodInfo(goodsInfo);
    }

    // 兑换按钮的点击事件
    public void onClick(SearchRespBean.SearchRespItemBean item) {
        mListener.onClick(item);
    }
}

























