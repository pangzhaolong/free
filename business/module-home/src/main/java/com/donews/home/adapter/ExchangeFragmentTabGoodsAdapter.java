package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donews.base.widget.CenterImageSpan;
import com.donews.home.R;
import com.donews.home.databinding.HomeExchanageFragmentGoodsItemBinding;
import com.donews.middle.base.BaseBindingAdapter;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class ExchangeFragmentTabGoodsAdapter extends
        BaseBindingAdapter<HomeGoodsBean.GoodsInfo, HomeExchanageFragmentGoodsItemBinding> implements View.OnClickListener {

    public interface GoodsClickListener {
        /**
         * 对话按钮的点击
         *
         * @param item
         */
        void onExchanageClick(HomeGoodsBean.GoodsInfo item);
    }

    private final Context mContext;
    private final List<HomeGoodsBean.GoodsInfo> mGoodsList = new ArrayList<>();
    private GoodsClickListener mListener;

    public ExchangeFragmentTabGoodsAdapter(Context context, GoodsClickListener listener) {
        super(R.layout.home_exchanage_fragment_goods_item);
        this.mContext = context;
        this.mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<HomeGoodsBean.GoodsInfo> list, boolean needClear) {
        if (needClear) {
            mGoodsList.clear();
        }
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NonNull BaseBindViewHolder<HomeExchanageFragmentGoodsItemBinding> homeExchanageFragmentGoodsItemBindingBaseBindViewHolder, @Nullable HomeGoodsBean.GoodsInfo goodsInfo) {

    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        HomeGoodsBean.GoodsInfo gi = (HomeGoodsBean.GoodsInfo) v.getTag();
        mListener.onExchanageClick(gi);
    }
}

























