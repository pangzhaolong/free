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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.doing.spike.R;
import com.doing.spike.bean.CombinationSpikeBean;
import com.doing.spike.bean.SpikeBean;
import com.doing.spike.databinding.SpikeContextItemBinding;

import java.util.List;

public class SpikeContextAdapter extends RecyclerView.Adapter<SpikeContextAdapter.SpikeViewHolder> {

    private int mLayoutId;
    private CombinationSpikeBean mCombinationSpikeBean;
    private Context mContext;

    SpikeBean.RoundsListDTO mRoundsListDTO;
    public SpikeContextAdapter(Context context ) {
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
      SpikeBean.GoodsListDTO goodsListDTO= mCombinationSpikeBean.getSpikeBean().getGoodsList().get(position);
        holder.mSpikeContextItemBinding.setCommodity(goodsListDTO);
        RoundedCorners roundedCorners = new RoundedCorners(5);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(mContext).load(mCombinationSpikeBean.getSpikeBean().getGoodsList().get(position).getMainPic()).apply(options).into(holder.mSpikeContextItemBinding.picture);
        holder.mSpikeContextItemBinding.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if(mCombinationSpikeBean.getRoundsListDTO()!=null&& mCombinationSpikeBean.getRoundsListDTO().getStatus()==2){
            holder.mSpikeContextItemBinding.grabbedBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.spike_green_shape));
            holder.mSpikeContextItemBinding.spikeProgress.setVisibility(View.GONE);
            holder.mSpikeContextItemBinding.setPeriod(mCombinationSpikeBean.getRoundsListDTO());
        }else{
            holder.mSpikeContextItemBinding.grabbedBg.setBackgroundDrawable(mContext.getDrawable(R.drawable.spike_quantity_shape));
            holder.mSpikeContextItemBinding.spikeProgress.setVisibility(View.VISIBLE);
            holder.mSpikeContextItemBinding.setPeriod(mCombinationSpikeBean.getRoundsListDTO());
        }

    }


    @Override
    public int getItemCount() {
        return (mCombinationSpikeBean != null) && (mCombinationSpikeBean.getSpikeBean().getGoodsList() != null) ? mCombinationSpikeBean.getSpikeBean().getGoodsList().size() : 0;
    }

    public void setCombinationSpikeBean(CombinationSpikeBean bean) {
        this.mCombinationSpikeBean = bean;
    }

    class SpikeViewHolder extends RecyclerView.ViewHolder {
        SpikeContextItemBinding mSpikeContextItemBinding;

        public SpikeViewHolder(@NonNull SpikeContextItemBinding spikeContextItemBinding) {
            super(spikeContextItemBinding.getRoot());
            this.mSpikeContextItemBinding = spikeContextItemBinding;

        }
    }
}
