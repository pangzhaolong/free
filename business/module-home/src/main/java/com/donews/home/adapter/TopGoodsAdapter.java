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

import java.util.ArrayList;
import java.util.List;

public class TopGoodsAdapter extends RecyclerView.Adapter<TopGoodsAdapter.GoodsViewHolder> {

    private final Context mContext;
    private final List<TopGoodsBean.goodsInfo> mGoodsList = new ArrayList<>();

    public TopGoodsAdapter(Context context) {
        mContext = context;
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

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {

        Glide.with(mContext).load(mGoodsList.get(position).getMainPic()).into(holder.getPicIv());
    }


    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picIv;
        private final TextView desTv;
        private final TextView giftTv;
        private final TextView salesTv;

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
            giftTv = itemView.findViewById(R.id.home_top_goods_item_gift);
            salesTv = itemView.findViewById(R.id.home_top_goods_item_sales);
        }
    }
}

























