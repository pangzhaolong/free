package com.donews.front.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donews.front.R;
import com.donews.middle.bean.front.FrontConfigBean;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class BannerViewAdapter extends BaseBannerAdapter<FrontConfigBean.BannerItem> {

    private final Context mContext;

    public BannerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected void bindData(BaseViewHolder<FrontConfigBean.BannerItem> holder, FrontConfigBean.BannerItem data, int position, int pageSize) {
        Glide.with(mContext).load(data.getImg()).into((ImageView) holder.findViewById(R.id.front_banner_item_img));
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.front_banner_item_view;
    }

}
