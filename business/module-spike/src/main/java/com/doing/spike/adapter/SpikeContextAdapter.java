/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */


package com.doing.spike.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doing.spike.R;
import com.doing.spike.bean.SpikeBean;
import com.doing.spike.databinding.SpikeContextItemBinding;

public class SpikeContextAdapter extends RecyclerView.Adapter<SpikeContextAdapter.SpikeViewHolder> {

    private int mLayoutId;
    private SpikeBean mSpikeBeans;
    private Context mContext;

    public SpikeContextAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public SpikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SpikeContextItemBinding spikeContextItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);

        SpikeViewHolder holder = new SpikeViewHolder(spikeContextItemBinding);
        return holder;
    }

    public void getLayout(int layoutId) {
        this.mLayoutId = layoutId;
    }

    public void onBindViewHolder(@NonNull SpikeViewHolder holder, int position) {
        holder.mSpikeContextItemBinding.setCommodity(mSpikeBeans.getGoodsList().get(position));
        Glide.with(mContext).load(mSpikeBeans.getGoodsList().get(position).getMainPic()).into(holder.mSpikeContextItemBinding.picture);
        holder.mSpikeContextItemBinding.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return mSpikeBeans != null && mSpikeBeans.getGoodsList() != null ? mSpikeBeans.getGoodsList().size() : 0;
    }

    public void setSpikeBeans(SpikeBean mSpikeBeans) {
        this.mSpikeBeans = mSpikeBeans;
    }

    class SpikeViewHolder extends RecyclerView.ViewHolder {
        SpikeContextItemBinding mSpikeContextItemBinding;

        public SpikeViewHolder(@NonNull SpikeContextItemBinding spikeContextItemBinding) {
            super(spikeContextItemBinding.getRoot());
            this.mSpikeContextItemBinding = spikeContextItemBinding;

        }
    }
}
