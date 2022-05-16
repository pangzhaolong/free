package com.donews.collect.adapter

import com.donews.base.utils.glide.GlideUtils
import com.donews.collect.bean.GoodBean
import com.donews.collect.databinding.CollectItemGoodBinding
import com.donews.library_recyclerview.BaseRecyclerViewAdapter
import com.donews.library_recyclerview.DataBindBaseViewHolder

/**
 *  make in st
 *  on 2022/5/16 16:25
 */
class GoodAdapter(resId: Int, dataList: MutableList<GoodBean>) :
    BaseRecyclerViewAdapter<GoodBean, DataBindBaseViewHolder>(resId, dataList) {

    private var collectBinding: CollectItemGoodBinding? = null
    var clickCall: (curNum:Int) ->Unit = {}

    override fun convert(viewHolder: DataBindBaseViewHolder?, data: GoodBean?, position: Int) {
        collectBinding = viewHolder?.getBinding() as CollectItemGoodBinding
        if (data?.mainPic != null && data.mainPic != ""){
            GlideUtils.loadImageView(mContext,data.mainPic,collectBinding?.image)
        }
        collectBinding?.tv?.text = data?.title
    }
}