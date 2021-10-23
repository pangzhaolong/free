package com.donews.mine.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.mine.R;
import com.donews.mine.bean.RewardHistoryBean;
import com.donews.mine.listener.RewardItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MineRewarHistoryAdapter extends RecyclerView.Adapter<MineRewarHistoryAdapter.RewardHistoryViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<RewardHistoryBean.RewardBean> mList = new ArrayList<>();
    private RewardItemClickListener mListener;

    public MineRewarHistoryAdapter(Context context, RewardItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<RewardHistoryBean.RewardBean> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RewardHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_reward_history_item, parent, false);
        return new RewardHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardHistoryViewHolder holder, int position) {
        RewardHistoryBean.RewardBean rewardBean = mList.get(position);
        if (rewardBean == null) {
            return;
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.titleTv.setText(rewardBean.getPeriod()+"期开奖码");
        SpannableString spannableString = new SpannableString(rewardBean.getCode());
        if (spannableString.length() != 7) {
            return;
        }
        for (int i = 0; i< spannableString.length(); i++) {
            holder.textViews[i].setText(spannableString.subSequence(i, i+1));
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (position < 0 || position >= mList.size()) {
            return;
        }

        RewardHistoryBean.RewardBean rewardBean = mList.get(position);

        mListener.onClick(rewardBean.getPeriod());
    }

    protected static class RewardHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView[] textViews = new TextView[7];
        private final TextView titleTv;

        public RewardHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.mine_reward_title_tv);
            textViews[0] = itemView.findViewById(R.id.mine_reward_num_1_tv);
            textViews[1] = itemView.findViewById(R.id.mine_reward_num_2_tv);
            textViews[2] = itemView.findViewById(R.id.mine_reward_num_3_tv);
            textViews[3] = itemView.findViewById(R.id.mine_reward_num_4_tv);
            textViews[4] = itemView.findViewById(R.id.mine_reward_num_5_tv);
            textViews[5] = itemView.findViewById(R.id.mine_reward_num_6_tv);
            textViews[6] = itemView.findViewById(R.id.mine_reward_num_7_tv);
        }
    }
}
