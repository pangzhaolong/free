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
import com.donews.front.R;
import com.donews.front.bean.FrontGoodsBean;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class GiftGoodsAdapter extends RecyclerView.Adapter<GiftGoodsAdapter.GoodsViewHolder> {
    private final List<FrontGoodsBean> mGoodsList = new ArrayList<>();

    private Context mContext;

    public GiftGoodsAdapter(Context context) {
        mContext = context;
        mGoodsList.add(new FrontGoodsBean("//img.alicdn.com/imgextra/i1/3572132423/O1CN010QeFcK1TlmFbKugvY_!!3572132423.jpg",
                "沃隆每日蔓越莓干360g*12袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i4/4207676188/O1CN01JHGu0C1va9RqQY6mo_!!4207676188.jpg",
                "【秒杀价￥7.98】淘小妞蛋黄酥6枚",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i2/3942475925/O1CN01R1j6Wg1tdhOLrJomg_!!3942475925.jpg",
                "【拍5件】好日子网红岩烧饼干5袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i1/3408811645/O1CN01G0O75R1O1S8avMbBv_!!3408811645-0-lubanu-s.jpg",
                "【凤祥食品】咸蛋黄嫩骨鸡340g*3",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i4/2745562235/O1CN01ywszlB1SNfuVWJMUv_!!2745562235-0-lubanu-s.jpg",
                "【薇娅推荐】沃隆开心果休闲零食500g袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i1/1654050000/O1CN01QFDTiD1Bs2fVBCfF0_!!1654050000.jpg",
                "【拍三件】香氛控油去屑洗发露",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("//img.alicdn.com/imgextra/i1/3572132423/O1CN010QeFcK1TlmFbKugvY_!!3572132423.jpg",
                "沃隆每日蔓越莓干360g*12袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i4/4207676188/O1CN01JHGu0C1va9RqQY6mo_!!4207676188.jpg",
                "【秒杀价￥7.98】淘小妞蛋黄酥6枚",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i2/3942475925/O1CN01R1j6Wg1tdhOLrJomg_!!3942475925.jpg",
                "【拍5件】好日子网红岩烧饼干5袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i1/3408811645/O1CN01G0O75R1O1S8avMbBv_!!3408811645-0-lubanu-s.jpg",
                "【凤祥食品】咸蛋黄嫩骨鸡340g*3",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i4/2745562235/O1CN01ywszlB1SNfuVWJMUv_!!2745562235-0-lubanu-s.jpg",
                "【薇娅推荐】沃隆开心果休闲零食500g袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i1/1654050000/O1CN01QFDTiD1Bs2fVBCfF0_!!1654050000.jpg",
                "【拍三件】香氛控油去屑洗发露",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("//img.alicdn.com/imgextra/i1/3572132423/O1CN010QeFcK1TlmFbKugvY_!!3572132423.jpg",
                "沃隆每日蔓越莓干360g*12袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i4/4207676188/O1CN01JHGu0C1va9RqQY6mo_!!4207676188.jpg",
                "【秒杀价￥7.98】淘小妞蛋黄酥6枚",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i2/3942475925/O1CN01R1j6Wg1tdhOLrJomg_!!3942475925.jpg",
                "【拍5件】好日子网红岩烧饼干5袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i1/3408811645/O1CN01G0O75R1O1S8avMbBv_!!3408811645-0-lubanu-s.jpg",
                "【凤祥食品】咸蛋黄嫩骨鸡340g*3",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i4/2745562235/O1CN01ywszlB1SNfuVWJMUv_!!2745562235-0-lubanu-s.jpg",
                "【薇娅推荐】沃隆开心果休闲零食500g袋",
                1678, "0元抢购"));
        mGoodsList.add(new FrontGoodsBean("https://img.alicdn.com/imgextra/i1/1654050000/O1CN01QFDTiD1Bs2fVBCfF0_!!1654050000.jpg",
                "【拍三件】香氛控油去屑洗发露",
                1678, "0元抢购"));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<FrontGoodsBean> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_goods_item_h, parent, false);
        final GoodsViewHolder holder = new GoodsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        FrontGoodsBean goodsBean = mGoodsList.get(position);
        if (goodsBean == null) {
            return;
        }

        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(goodsBean.getMainPic())).into(holder.mainIv);
        holder.titleTv.setText(goodsBean.getTitle());
        holder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.priceTv.setText("￥ " + goodsBean.getPrice());
        holder.doTv.setText(goodsBean.getDoWhat());
    }


    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    protected static class GoodsViewHolder extends RecyclerView.ViewHolder {
        private ImageView mainIv;
        private TextView titleTv;
        private TextView priceTv;
        private TextView doTv;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            mainIv = itemView.findViewById(R.id.front_goods_item_iv);
            titleTv = itemView.findViewById(R.id.front_goods_item_des_tv);
            priceTv = itemView.findViewById(R.id.front_goods_item_price_tv);
            doTv = itemView.findViewById(R.id.front_item_do_tv);
        }
    }
}
