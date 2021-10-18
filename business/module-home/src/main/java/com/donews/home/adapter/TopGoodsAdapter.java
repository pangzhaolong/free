package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donews.home.R;
import com.donews.home.bean.TopGoodsBean;
import com.donews.home.listener.GoodsDetailListener;

import java.util.ArrayList;
import java.util.List;

public class TopGoodsAdapter extends RecyclerView.Adapter<TopGoodsAdapter.GoodsViewHolder> implements View.OnClickListener {

    private final Context mContext;
    private final List<TopGoodsBean.goodsInfo> mGoodsList = new ArrayList<>();
    private GoodsDetailListener mListener;

    public TopGoodsAdapter(Context context, GoodsDetailListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<TopGoodsBean.goodsInfo> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragent_top_goods_item, parent, false);
        final GoodsViewHolder holder = new GoodsViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {

        TopGoodsBean.goodsInfo gi = mGoodsList.get(position);
        if (gi == null) {
            return;
        }

        holder.itemView.setTag(gi);
        holder.itemView.setOnClickListener(this::onClick);
        Glide.with(mContext).load(gi.getMain_pic()).into(holder.picIv);
        holder.desTv.setText(gi.getDtitle());

        float sales = gi.getMonth_sales();
        if (sales >= 10000) {
            sales /= 10000;
            String strSales = String.format("%.1f", sales);
            holder.salesTv.setText("已售" + strSales + "万");
        } else {
            holder.salesTv.setText("已售" + gi.getMonth_sales());
        }
        holder.priceTv.setText(gi.getActual_price() + "");
        holder.giftTv.setText(gi.getCoupon_price()+"元");
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        TopGoodsBean.goodsInfo gi = (TopGoodsBean.goodsInfo) v.getTag();

        mListener.onClick(gi.getId(), gi.getGoods_id());
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picIv;
        private final TextView desTv;
        private final TextView priceTv;
        private final TextView salesTv;
        private final TextView giftTv;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);

            picIv = itemView.findViewById(R.id.home_top_goods_item_iv);
            desTv = itemView.findViewById(R.id.home_top_goods_item_des);
            priceTv = itemView.findViewById(R.id.home_top_goods_item_price_atv);
            salesTv = itemView.findViewById(R.id.home_top_goods_item_sales);
            giftTv = itemView.findViewById(R.id.home_top_goods_item_gift_atv);
        }
    }
}
