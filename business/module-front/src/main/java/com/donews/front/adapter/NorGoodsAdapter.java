package com.donews.front.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donews.front.NorClickListener;
import com.donews.front.R;
import com.donews.front.bean.FrontGoodsBean;
import com.donews.front.bean.NorGoodsBean;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class NorGoodsAdapter extends RecyclerView.Adapter<NorGoodsAdapter.GoodsViewHolder> implements View.OnClickListener {
    private List<NorGoodsBean.GoodsInfo> mGoodsList = new ArrayList<>();

    private Context mContext;
    private NorClickListener mListener;
    private int mCols;

    public NorGoodsAdapter(Context context, NorClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setCols(int cols) {
        mCols = cols;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<NorGoodsBean.GoodsInfo> list, boolean needClear) {
        if (needClear) {
            mGoodsList.clear();
        }
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }

    public void refreshItem(int position, String goodsId, int lotteryStatus) {
        if (position < 0 || position >= mGoodsList.size()) {
            return;
        }

        if (!mGoodsList.get(position).getGoodsId().equalsIgnoreCase(goodsId)) {
            return;
        }

        mGoodsList.get(position).setLotteryStatus(lotteryStatus);

        notifyItemChanged(position, "lotteryStatus");
    }

    public void clear() {
        if (mGoodsList != null) {
            mGoodsList.clear();
            mGoodsList = null;
        }
    }

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mCols == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_goods_item_h, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_goods_item_v, parent, false);
        }
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.size() <= 0) {
            return;
        }
        if (payloads.get(0).equals("lotteryStatus")) {
            NorGoodsBean.GoodsInfo goodsInfo = mGoodsList.get(position);
            if (goodsInfo == null) {
                return;
            }
            switch (goodsInfo.getLotteryStatus()) {
                case 0:
                    holder.doTv.setText("0元抽奖");
                    break;
                case 1:
                    holder.doTv.setText("继续参与");
                    break;
                case 2:
                    holder.doTv.setText("等待开奖");
                    break;
            }
            holder.doTv.postInvalidate();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        NorGoodsBean.GoodsInfo goodsInfo = mGoodsList.get(position);
        if (goodsInfo == null) {
            return;
        }

        holder.itemView.setOnClickListener(this::onClick);
        holder.itemView.setTag(position);
        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(holder.mainIv);
        holder.titleTv.setText(goodsInfo.getTitle());
        holder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.priceTv.setText("￥ " + goodsInfo.getOriginalPrice());
        switch (goodsInfo.getLotteryStatus()) {
            case 0:
                holder.doTv.setText("0元抽奖");
                break;
            case 1:
                holder.doTv.setText("继续参与");
                break;
            case 2:
                holder.doTv.setText("等待开奖");
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (position < 0 || position >= mGoodsList.size()) {
            return;
        }

        NorGoodsBean.GoodsInfo goodsInfo = mGoodsList.get(position);

        mListener.onClick(position, goodsInfo.getGoodsId());
    }

    protected static class GoodsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mainIv;
        private final TextView titleTv;
        private final TextView priceTv;
        private final TextView doTv;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            mainIv = itemView.findViewById(R.id.front_goods_item_iv);
            titleTv = itemView.findViewById(R.id.front_goods_item_des_tv);
            priceTv = itemView.findViewById(R.id.front_goods_item_price_tv);
            doTv = itemView.findViewById(R.id.front_item_do_tv);
        }
    }
}
