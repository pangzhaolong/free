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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donews.base.widget.CenterImageSpan;
import com.donews.home.R;
import com.donews.home.listener.GoodsClickListener;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class BuysGoodsAdapter extends RecyclerView.Adapter<BuysGoodsAdapter.BuysViewHolder> implements View.OnClickListener {

    private final Context mContext;
    private final List<HomeGoodsBean.GoodsInfo> mGoodsList = new ArrayList<>();
    private GoodsClickListener mListener;

    public BuysGoodsAdapter(Context context, GoodsClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<HomeGoodsBean.GoodsInfo> list, boolean needClear) {
        if (needClear) {
            mGoodsList.clear();
        }
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BuysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_search_buys_item, parent, false);
        final BuysViewHolder holder = new BuysViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BuysViewHolder holder, int position) {

        HomeGoodsBean.GoodsInfo gi = mGoodsList.get(position);
        if (gi == null) {
            return;
        }

        holder.itemView.setTag(gi);
        holder.itemView.setOnClickListener(this::onClick);
        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(gi.getMainPic())).into(holder.picIv);
        holder.desTv.setText(getTitleString(gi));

        holder.priceTv.setText("￥" + gi.getOriginalPrice());
        holder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.giftTv.setText(String.format("￥%.0f元券", gi.getCouponPrice()));
        holder.actualPriceTv.setText("￥" + gi.getActualPrice());
    }

    private SpannableString getTitleString(HomeGoodsBean.GoodsInfo goodsInfo) {
        String span = "d ";
        int resId = R.drawable.home_logo_tb;
        switch (goodsInfo.getSrc()) {
            case 1:
                resId = R.drawable.home_logo_tb;
                break;
            case 2:
                resId = R.drawable.home_logo_pdd;
                break;
            case 3:
                resId = R.drawable.home_logo_jd;
                break;
            case 4:
                break;
        }
        SpannableString spannableString = new SpannableString(span + goodsInfo.getTitle());

        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        if (drawable != null) {
            //计算大小，使其和文字高度一般一致
            float height = mContext.getResources().getDimension(R.dimen.home_title) * 0.85f;
            float width = height / drawable.getIntrinsicHeight() * drawable.getIntrinsicWidth();
            drawable.setBounds(0, 0, (int) width, (int) height);
        }
        CenterImageSpan imageSpan = new CenterImageSpan(drawable);
        spannableString.setSpan(imageSpan, 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        HomeGoodsBean.GoodsInfo gi = (HomeGoodsBean.GoodsInfo) v.getTag();

        mListener.onClick(gi.getGoodsId(), gi.getMaterialId(), gi.getSearchId(), gi.getSrc());
    }

    public static class BuysViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picIv;
        private final TextView desTv;
        private final TextView priceTv;
        private final TextView actualPriceTv;
        private final TextView giftTv;

        public BuysViewHolder(@NonNull View itemView) {
            super(itemView);

            picIv = itemView.findViewById(R.id.home_top_goods_item_iv);
            desTv = itemView.findViewById(R.id.home_top_goods_item_des);
            priceTv = itemView.findViewById(R.id.home_top_goods_item_price_atv);
            actualPriceTv = itemView.findViewById(R.id.home_top_goods_item_price);
            giftTv = itemView.findViewById(R.id.home_top_goods_item_gift_atv);
        }
    }
}
