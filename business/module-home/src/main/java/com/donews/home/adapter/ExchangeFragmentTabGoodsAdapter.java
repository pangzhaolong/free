package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.home.R;
import com.donews.home.databinding.HomeExchanageFragmentGoodsItemBinding;
import com.donews.middle.base.BaseBindingAdapter;
import com.donews.middle.bean.home.HomeExchangeGoodsBean;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchRespBean;

import java.util.List;

public class ExchangeFragmentTabGoodsAdapter extends
        BaseBindingAdapter<SearchRespBean.SearchRespItemBean, HomeExchanageFragmentGoodsItemBinding> {

    public interface GoodsClickListener {
        /**
         * 对话按钮的点击
         *
         * @param item
         */
        void onExchanageClick(SearchRespBean.SearchRespItemBean item);
    }

    private final Context mContext;
    private GoodsClickListener mListener;

    public ExchangeFragmentTabGoodsAdapter(Context context, GoodsClickListener listener) {
        super(R.layout.home_exchanage_fragment_goods_item);
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
    protected void convert(@NonNull BaseBindViewHolder<HomeExchanageFragmentGoodsItemBinding> helper,
                           @Nullable SearchRespBean.SearchRespItemBean goodsInfo) {
        helper.binding.setThiz(this);
        helper.binding.setGoodInfo(goodsInfo);
    }

    // 兑换按钮的点击事件
    public void onExchanageClick(SearchRespBean.SearchRespItemBean item) {
        mListener.onExchanageClick(item);
    }
}

























