package com.donews.detail.adapter

import com.bumptech.glide.Glide
import com.donews.detail.R
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 * 商品图片适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/13 11:05
 */
class GoodsBannerAdapter : BaseBannerAdapter<String>() {

    override fun bindData(holder: BaseViewHolder<String>, data: String, position: Int, pageSize: Int) {
        Glide.with(holder.itemView.context).load(data).into(holder.findViewById(R.id.iv_img))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.detail_item_goods_banner
    }
}