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
import com.donews.home.bean.NorGoodsBean;
import com.donews.home.bean.TopGoodsBean;
import com.donews.home.listener.GoodsDetailListener;

import java.util.ArrayList;
import java.util.List;

public class NorGoodsAdapter extends RecyclerView.Adapter<NorGoodsAdapter.GoodsViewHolder> implements View.OnClickListener{

    private final Context mContext;
    private final List<NorGoodsBean.goodsInfo> mGoodsList = new ArrayList<>();
    private GoodsDetailListener mListener;

    public NorGoodsAdapter(Context context, GoodsDetailListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<NorGoodsBean.goodsInfo> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragent_nor_goods_item, parent, false);
        final GoodsViewHolder holder = new GoodsViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {

        NorGoodsBean.goodsInfo gi = mGoodsList.get(position);
        if (gi == null) {
            return;
        }

        holder.itemView.setTag(gi);
        holder.itemView.setOnClickListener(this::onClick);

        Glide.with(mContext).load(gi.getMain_pic()).into(holder.getPicIv());
        holder.desTv.setText(gi.getTitle());
        holder.salesTv.setText("已售" + gi.getMonth_sales());
        holder.priceTv.setText(gi.getActual_price() + "");
        holder.shopTv.setText(gi.getShop_name());
    }


    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        NorGoodsBean.goodsInfo gi = (NorGoodsBean.goodsInfo) v.getTag();

        mListener.onClick(gi.getId(), gi.getGoods_id());
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picIv;
        private final TextView desTv;
        private final TextView priceTv;
        private final TextView giftTv;
        private final TextView salesTv;
        private final TextView shopTv;

        public ImageView getPicIv() {
            return picIv;
        }


        public TextView getDesTv() {
            return desTv;
        }


        public TextView getGiftTv() {
            return giftTv;
        }


        public TextView getSalesTv() {
            return salesTv;
        }


        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);

            picIv = itemView.findViewById(R.id.home_top_goods_item_pic);
            desTv = itemView.findViewById(R.id.home_top_goods_item_des);
            priceTv = itemView.findViewById(R.id.home_top_goods_item_price_atv);
            giftTv = itemView.findViewById(R.id.home_top_goods_item_gift);
            salesTv = itemView.findViewById(R.id.home_top_goods_item_sales);
            shopTv = itemView.findViewById(R.id.home_top_goods_item_shop_atv);
        }
    }
}

























