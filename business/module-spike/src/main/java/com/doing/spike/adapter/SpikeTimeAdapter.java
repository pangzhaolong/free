/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */


package com.doing.spike.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doing.spike.R;
import com.doing.spike.bean.SpikeBean;
import com.doing.spike.util.CenterLayoutManager;

public class SpikeTimeAdapter extends RecyclerView.Adapter<SpikeTimeAdapter.SpikeViewHolder> {
    private SpikeBean mSpikeBeans;
    private int mLayoutId;
    private Context mContext;
    private IClickCallbackListener mClickListener;
    private CenterLayoutManager mCenterLayoutManager;
    private RecyclerView mRecyclerView;
    private SpikeViewHolder mPanicBuying;

    public SpikeTimeAdapter(Context context, CenterLayoutManager centerLayoutManager, RecyclerView recyclerView) {
        this.mContext = context.getApplicationContext();
        this.mCenterLayoutManager = centerLayoutManager;
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public SpikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        SpikeViewHolder holder = new SpikeViewHolder(view);
        slidePosition();
        return holder;
    }


    /**
     * * 方法用来打开应用时自动定位到当前疯抢中
     */
    public void slidePosition() {
        if (mCenterLayoutManager != null) {
            for (int i = 0; i < mSpikeBeans.getRoundsList().size(); i++) {
                if (mSpikeBeans.getRoundsList().get(i).getStatus() == 1) {
                    mCenterLayoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), i);
                    break;
                }
            }
        }
    }


    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull SpikeViewHolder holder, int position) {
        SpikeBean.RoundsListDTO roundsListDTO = mSpikeBeans.getRoundsList().get(position);
        holder.mtimeShow.setText(roundsListDTO.getRound() + "场");
        holder.mSpikeType.setText(roundsListDTO.getStatus() == 0 ? "已开抢" : roundsListDTO.getStatus() == 1 ? "疯抢中" :
                roundsListDTO.getStatus() == 2 ? "即将开始" : "");
        //把当前疯抢中的 holder 存为全局变量，当点击时，需要把 这个holder 修改为未选中
        if (roundsListDTO.getStatus() == 1 && mPanicBuying == null) {
            setSelectType(holder);
            mPanicBuying = holder;
            //这是点
        } else if (mPanicBuying != null && mPanicBuying.mtimeShow.getText().toString().equals(roundsListDTO.getDdqTime())) {
            setSelectType(holder);
            mPanicBuying = holder;
        } else {
            unSetSelectType(holder);
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止重复点击
                if ((mPanicBuying != null) && (mPanicBuying == holder)) {
                    return;
                }
                setSelectType(holder);
                if (mPanicBuying != null) {
                    unSetSelectType(mPanicBuying);
                    mPanicBuying = null;
                }
                mPanicBuying = holder;
                mCenterLayoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), position);
                //点击后的事件回调
                if (mClickListener != null) {
                    mClickListener.onClick(roundsListDTO);
                }
            }
        });
    }


    private void setSelectType(SpikeViewHolder holder) {
        holder.mtimeShow.setTextColor(mContext.getResources().getColor(R.color.selected_color));
        holder.mSpikeType.setTextColor(Color.WHITE);
        holder.mSpikeType.setBackground(mContext.getResources().getDrawable(R.drawable.spike_button_shape_corner));
    }

    private void unSetSelectType(SpikeViewHolder holder) {
        holder.mtimeShow.setTextColor(mContext.getResources().getColor(R.color.un_time_color));
        holder.mSpikeType.setTextColor(mContext.getResources().getColor(R.color.un_time_title_color));
        holder.mSpikeType.setBackgroundColor(mContext.getResources().getColor(R.color.bg_color));
    }

    public SpikeBean getSpikeBeans() {
        return mSpikeBeans;
    }

    public void setSpikeBeans(SpikeBean mSpikeBeans) {
        this.mSpikeBeans = mSpikeBeans;
    }

    public void setClickListener(IClickCallbackListener onClickListener) {
        this.mClickListener = onClickListener;
    }

    public void getLayout(int layoutId) {
        this.mLayoutId = layoutId;
    }

    @Override
    public int getItemCount() {
        return mSpikeBeans != null && mSpikeBeans.getRoundsList() != null ? mSpikeBeans.getRoundsList().size() : 0;
    }

    class SpikeViewHolder extends RecyclerView.ViewHolder {
        TextView mtimeShow;
        TextView mSpikeType;
        LinearLayout rootView;

        public SpikeViewHolder(@NonNull View itemView) {
            super(itemView);
            mtimeShow = itemView.findViewById(R.id.time_show);
            mSpikeType = itemView.findViewById(R.id.spike_type);
            rootView = itemView.findViewById(R.id.root_view);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }


    public interface IClickCallbackListener {
        void onClick(SpikeBean.RoundsListDTO roundsListDTO);
    }
}

