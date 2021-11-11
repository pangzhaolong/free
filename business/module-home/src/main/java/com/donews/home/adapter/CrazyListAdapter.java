package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.donews.home.listener.GoodsDetailListener;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class CrazyListAdapter extends RecyclerView.Adapter<CrazyListAdapter.CrazyListViewHolder> implements View.OnClickListener {

    private final Context mContext;
    private final List<RealTimeBean.goodsInfo> mGoodsList = new ArrayList<>();
    private final GoodsDetailListener mListener;

    public CrazyListAdapter(Context context, GoodsDetailListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<RealTimeBean.goodsInfo> list, boolean isAdd) {
        if (!isAdd) {
            mGoodsList.clear();
        }
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CrazyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragent_crazy_goods_item, parent, false);
        final CrazyListViewHolder holder = new CrazyListViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CrazyListViewHolder holder, int position) {

        RealTimeBean.goodsInfo gi = mGoodsList.get(position);
        if (gi == null) {
            return;
        }

        holder.itemView.setTag(gi);
        holder.itemView.setOnClickListener(this::onClick);

        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(gi.getMainPic())).into(holder.picIv);
        holder.desTv.setText(getTitleString(gi));


        holder.priceTv.setText("￥" + gi.getActualPrice());
        holder.giftTv.setText("￥" + gi.getCouponPrice() + "元券");
    }

    private SpannableString getTitleString(RealTimeBean.goodsInfo goodsInfo) {
        String span = "d ";
        int resId = goodsInfo.getShopType() == 1 ? R.drawable.home_logo_tm : R.drawable.home_logo_tb;
        SpannableString spannableString = new SpannableString(span + goodsInfo.getDtitle());

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
        RealTimeBean.goodsInfo gi = (RealTimeBean.goodsInfo) v.getTag();

        mListener.onClick(gi.getId(), gi.getGoodsId());
    }

    public static class CrazyListViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picIv;
        private final TextView desTv;
        private final TextView priceTv;
        private final TextView giftTv;

        public CrazyListViewHolder(@NonNull View itemView) {
            super(itemView);

            picIv = itemView.findViewById(R.id.home_crazy_goods_item_pic_iv);
            desTv = itemView.findViewById(R.id.home_crazy_goods_item_des);
            priceTv = itemView.findViewById(R.id.home_crazy_goods_item_price);
            giftTv = itemView.findViewById(R.id.home_crzay_goods_item_gift_atv);
        }
    }
}

























