package com.donews.front.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donews.front.R;
import com.donews.front.listener.FrontBannerClickListener;
import com.donews.middle.bean.front.FrontConfigBean;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class BannerViewAdapter extends BaseBannerAdapter<FrontConfigBean.YywItem> implements View.OnClickListener {

    private final Context mContext;

    private FrontBannerClickListener mListener;

    public BannerViewAdapter(Context context, FrontBannerClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void bindData(BaseViewHolder<FrontConfigBean.YywItem> holder, FrontConfigBean.YywItem data, int position, int pageSize) {
        Glide.with(mContext).load(data.getImg()).into((ImageView) holder.findViewById(R.id.front_banner_item_img));
        holder.setOnClickListener(R.id.front_banner_item_img, this);
        holder.findViewById(R.id.front_banner_item_img).setTag(position);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.front_banner_item_view;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        mListener.onClick(position);
    }
}
