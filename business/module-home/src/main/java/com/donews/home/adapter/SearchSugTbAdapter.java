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
import com.donews.home.bean.SearchResultTbBean;
import com.donews.home.listener.GoodsDetailListener;

import java.util.ArrayList;
import java.util.List;

public class SearchSugTbAdapter extends RecyclerView.Adapter<SearchSugTbAdapter.ResultViewHolder> implements View.OnClickListener {
    private final Context mContext;
    private final List<SearchResultTbBean.goodsInfo> mGoodsList = new ArrayList<>();
    private GoodsDetailListener mListener;

    public SearchSugTbAdapter(Context context, GoodsDetailListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<SearchResultTbBean.goodsInfo> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_search_result_tb_item, parent, false);
        final ResultViewHolder holder = new ResultViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {

        SearchResultTbBean.goodsInfo gi = mGoodsList.get(position);
        if (gi == null) {
            return;
        }

        holder.itemView.setTag(gi);
        holder.itemView.setOnClickListener(this::onClick);


        Glide.with(mContext).load(gi.getMainPic()).into(holder.getPicIv());
        holder.desTv.setText(gi.getTitle());
        holder.salesTv.setText("已售" + gi.getMonthSales());
        holder.priceTv.setText(gi.getActualPrice() + "");
        holder.shopTv.setText(gi.getShopName());
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        SearchResultTbBean.goodsInfo gi = (SearchResultTbBean.goodsInfo) v.getTag();

        mListener.onClick(gi.getId(), gi.getGoodsId());
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

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


        public ResultViewHolder(@NonNull View itemView) {
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
