package com.donews.home.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donews.home.R;
import com.donews.home.bean.BannerBean;
import com.donews.utilslibrary.utils.UrlUtils;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class TopBannerViewAdapter extends BaseBannerAdapter<BannerBean> {

    private final Context mContext;

    public TopBannerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected void bindData(BaseViewHolder<BannerBean> holder, BannerBean data, int position, int pageSize) {
        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(data.getTopic_image())).into((ImageView) holder.findViewById(R.id.home_top_banner_view_pager_iv));
//        holder.setText();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.home_banner_view_pager_item;
    }

}
