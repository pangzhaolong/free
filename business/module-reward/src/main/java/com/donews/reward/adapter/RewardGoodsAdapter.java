package com.donews.front.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class RewardGoodsAdapter extends RecyclerView.Adapter<RewardGoodsAdapter.GoodsViewHolder> {
//    private final List<FrontGoodsBean> mGoodsList = new ArrayList<>();

    private Context mContext;

    public RewardGoodsAdapter(Context context) {
        mContext = context;

    }
/*
    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<FrontGoodsBean> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_goods_item_h, parent, false);
        final GoodsViewHolder holder = new GoodsViewHolder(view);
        return holder;*/
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        /*FrontGoodsBean goodsBean = mGoodsList.get(position);
        if (goodsBean == null) {
            return;
        }

        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(goodsBean.getMainPic())).into(holder.mainIv);
        holder.titleTv.setText(goodsBean.getTitle());
        holder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.priceTv.setText("￥ " + goodsBean.getPrice());
        holder.doTv.setText(goodsBean.getDoWhat());*/
    }


    @Override
    public int getItemCount() {
//        return mGoodsList.size();
        return 0;
    }

    protected static class GoodsViewHolder extends RecyclerView.ViewHolder {
        private ImageView mainIv;
        private TextView titleTv;
        private TextView priceTv;
        private TextView doTv;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
//            mainIv = itemView.findViewById(R.id.front_goods_item_iv);
//            titleTv = itemView.findViewById(R.id.front_goods_item_des_tv);
//            priceTv = itemView.findViewById(R.id.front_goods_item_price_tv);
//            doTv = itemView.findViewById(R.id.front_item_do_tv);
        }
    }
}
