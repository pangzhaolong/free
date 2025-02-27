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

public class TopGoodsAdapter extends RecyclerView.Adapter<TopGoodsAdapter.GoodsViewHolder> implements View.OnClickListener {

    private final Context mContext;
    private final List<HomeGoodsBean.GoodsInfo> mGoodsList = new ArrayList<>();
    private GoodsClickListener mListener;

    public TopGoodsAdapter(Context context, GoodsClickListener listener) {
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
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_top_goods_item, parent, false);
        final GoodsViewHolder holder = new GoodsViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {

        HomeGoodsBean.GoodsInfo gi = mGoodsList.get(position);
        if (gi == null) {
            return;
        }

        holder.itemView.setTag(gi);
        holder.itemView.setOnClickListener(this::onClick);
        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(gi.getMainPic())).into(holder.picIv);
        holder.desTv.setText(getTitleString(gi));

        /*float sales = gi.getMonth_sales();
        if (sales >= 10000) {
            sales /= 10000;
            String strSales = String.format("%.1f", sales);
            holder.salesTv.setText("已售" + strSales + "万");
        } else {
            holder.salesTv.setText("已售" + gi.getMonth_sales());
        }*/
        holder.priceTv.setText("￥" + gi.getOriginalPrice());
        holder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.giftTv.setText("￥" + gi.getCouponPrice() + "元券");
        holder.actualPriveTv.setText("￥" + gi.getActualPrice());
    }

    private SpannableString getTitleString(HomeGoodsBean.GoodsInfo goodsInfo) {
        String span = "d ";
        int resId = R.drawable.home_logo_tm;
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

//        mListener.onClick(gi.getId(), gi.getGoods_id());
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picIv;
        private final TextView desTv;
        private final TextView priceTv;
        private final TextView actualPriveTv;
        private final TextView giftTv;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);

            picIv = itemView.findViewById(R.id.home_top_goods_item_iv);
            desTv = itemView.findViewById(R.id.home_top_goods_item_des);
            priceTv = itemView.findViewById(R.id.home_top_goods_item_price_atv);
            actualPriveTv = itemView.findViewById(R.id.home_top_goods_item_price);
            giftTv = itemView.findViewById(R.id.home_top_goods_item_gift_atv);
        }
    }
}
