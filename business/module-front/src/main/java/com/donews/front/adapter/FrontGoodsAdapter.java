package com.donews.front.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donews.front.R;
import com.donews.front.listener.FrontClickListener;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.middle.bean.front.LotteryGoodsBean;
import com.donews.utilslibrary.utils.DensityUtils;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrontGoodsAdapter extends RecyclerView.Adapter<FrontGoodsAdapter.GoodsViewHolder> implements View.OnClickListener {
    private List<LotteryGoodsBean.GoodsInfo> mGoodsList = new ArrayList<>();

    private Context mContext;
    private FrontClickListener mListener;
    private int mCols;
    private int mPadding = DensityUtils.dp2px(6);
    private boolean mIsOpenCriticalModel = false;

    public FrontGoodsAdapter(Context context, FrontClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setCols(int cols) {
        mCols = cols;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<LotteryGoodsBean.GoodsInfo> list, boolean needClear, boolean needKeepFirst) {
        if (needClear) {
            mGoodsList.clear();
            initTopList(list);
        } else {
            mGoodsList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void openCriticalModel(boolean open) {
        if (mGoodsList == null || mGoodsList.size() < 2) {
            return;
        }
        if (mIsOpenCriticalModel == open) {
            return;
        }

        mIsOpenCriticalModel = open;
        mGoodsList.get(0).setCritical_guid(open);
        mGoodsList.get(1).setCritical_guid(open);

        if (!open) {
            mGoodsList.get(0).setCritical_show_hand(open);
            mGoodsList.get(1).setCritical_show_hand(open);
            notifyItemChanged(0, "criticalGuid");
            notifyItemChanged(1, "criticalGuid");
        }
    }

    private void initTopList(List<LotteryGoodsBean.GoodsInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isTop()) {
                mGoodsList.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        Collections.shuffle(list);
        mGoodsList.addAll(list);
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

    public void startCriticalGuid(int position) {
        if (position < 0 || position >= mGoodsList.size()) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            mGoodsList.get(i).setCritical_show_hand(position == i);
            notifyItemChanged(i, "criticalGuid");
        }
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.size() <= 0) {
            return;
        }
        LotteryGoodsBean.GoodsInfo goodsInfo = mGoodsList.get(position);
        if (goodsInfo == null) {
            return;
        }
        if (payloads.get(0).equals("lotteryStatus")) {
            switch (goodsInfo.getLotteryStatus()) {
                case 0:
                    holder.doTv.setText(OtherSwitch.Ins().getLotteryPriceShow()+"元抽奖");
                    holder.labelIv.setVisibility(View.GONE);
                    holder.doTv.setBackgroundResource(R.drawable.front_goods_item_lottery_bg);
                    break;
                case 1:
                    holder.doTv.setText("继续参与");
                    holder.labelIv.setVisibility(View.GONE);
                    holder.doTv.setBackgroundResource(R.drawable.front_goods_item_lottery_bg);
                    break;
                case 2:
                    holder.doTv.setText("等待开奖");
                    holder.labelIv.setVisibility(View.VISIBLE);
                    holder.doTv.setBackgroundResource(R.drawable.front_goods_item_lottery_bg_disable);
                    break;
            }
            holder.doTv.postInvalidate();
        } else if (payloads.get(0).equals("criticalGuid")) {
            if (goodsInfo.isCritical_guid()) {
                holder.criticalClock.setVisibility(View.VISIBLE);
                if (goodsInfo.isCritical_show_hand()) {
                    holder.criticalHand.setVisibility(View.VISIBLE);
                } else {
                    holder.criticalHand.setVisibility(View.GONE);
                }
            } else {
                holder.criticalClock.setVisibility(View.GONE);
                holder.criticalHand.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        LotteryGoodsBean.GoodsInfo goodsInfo = mGoodsList.get(position);
        if (goodsInfo == null) {
            return;
        }

        holder.itemView.setOnClickListener(this::onClick);
        holder.itemView.setTag(position);
        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(holder.mainIv);
        holder.titleTv.setText(goodsInfo.getTitle());
        holder.priceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.priceTv.setText("￥ " + goodsInfo.getOriginalPrice());
        holder.edgeFl.setBackground(null);
        holder.tipTv.setVisibility(View.GONE);
        holder.edgeFl.setPadding(0, 0, 0, 0);
        switch (goodsInfo.getTag()) {
            case 1:
                holder.tipTv.setText("爆款推荐");
                holder.tipTv.setVisibility(View.VISIBLE);
                holder.tipTv.setBackgroundResource(R.drawable.front_goods_item_iv_tip_1);
                holder.edgeFl.setBackgroundResource(R.drawable.front_goods_item_iv_edge_1);
                holder.edgeFl.setPadding(mPadding, mPadding, mPadding, mPadding);
                break;
            case 2:
                holder.tipTv.setText("今日热门");
                holder.tipTv.setVisibility(View.VISIBLE);
                holder.tipTv.setBackgroundResource(R.drawable.front_goods_item_iv_tip_2);
                holder.edgeFl.setBackgroundResource(R.drawable.front_goods_item_iv_edge_2);
                holder.edgeFl.setPadding(mPadding, mPadding, mPadding, mPadding);
                break;
            case 3:
                holder.tipTv.setText("中奖最多");
                holder.tipTv.setVisibility(View.VISIBLE);
                holder.tipTv.setBackgroundResource(R.drawable.front_goods_item_iv_tip_3);
                holder.edgeFl.setBackgroundResource(R.drawable.front_goods_item_iv_edge_3);
                holder.edgeFl.setPadding(mPadding, mPadding, mPadding, mPadding);
                break;
        }

        switch (goodsInfo.getLotteryStatus()) {
            case 0:
                holder.doTv.setText(OtherSwitch.Ins().getLotteryPriceShow()+"元抽奖");
                holder.labelIv.setVisibility(View.GONE);
                holder.doTv.setBackgroundResource(R.drawable.front_goods_item_lottery_bg);
                break;
            case 1:
                holder.doTv.setText("继续参与");
                holder.labelIv.setVisibility(View.GONE);
                holder.doTv.setBackgroundResource(R.drawable.front_goods_item_lottery_bg);
                break;
            case 2:
                holder.doTv.setText("等待开奖");
                holder.labelIv.setVisibility(View.VISIBLE);
                holder.doTv.setBackgroundResource(R.drawable.front_goods_item_lottery_bg_disable);
                break;
        }

        if (goodsInfo.isCritical_guid()) {
            holder.criticalClock.setVisibility(View.VISIBLE);
            if (goodsInfo.isCritical_show_hand()) {
                holder.criticalHand.setVisibility(View.VISIBLE);
            }
        } else {
            holder.criticalClock.setVisibility(View.GONE);
            holder.criticalHand.setVisibility(View.GONE);
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

        LotteryGoodsBean.GoodsInfo goodsInfo = mGoodsList.get(position);

        mListener.onClick(position, goodsInfo.getGoodsId());
    }

    protected static class GoodsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mainIv;
        private final ImageView labelIv;
        private final TextView titleTv;
        private final TextView priceTv;
        private final TextView doTv;
        private final FrameLayout edgeFl;
        private final TextView tipTv;

        private final ImageView criticalClock;
        private final ImageView criticalHand;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            mainIv = itemView.findViewById(R.id.front_goods_item_iv);
            labelIv = itemView.findViewById(R.id.front_goods_item_label_iv);
            titleTv = itemView.findViewById(R.id.front_goods_item_des_tv);
            priceTv = itemView.findViewById(R.id.front_goods_item_price_tv);
            doTv = itemView.findViewById(R.id.front_item_do_tv);
            edgeFl = itemView.findViewById(R.id.front_goods_item_edge_fl);
            tipTv = itemView.findViewById(R.id.front_goods_item_tip_tv);

            criticalClock = itemView.findViewById(R.id.front_goods_item_clock_iv);
            criticalHand = itemView.findViewById(R.id.front_goods_item_guid_lav);
        }
    }
}
