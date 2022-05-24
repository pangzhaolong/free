package com.donews.collect.adapter

import android.annotation.SuppressLint
import com.donews.base.utils.glide.GlideUtils
import com.donews.base.utils.glide.RoundCornersTransform
import com.donews.collect.bean.CardFragment
import com.donews.collect.databinding.CollectItemFragmentBinding
import com.donews.library_recyclerview.BaseRecyclerViewAdapter
import com.donews.library_recyclerview.DataBindBaseViewHolder
import com.donews.utilslibrary.utils.DensityUtils
import java.lang.Exception

/**
 *  make in st
 *  on 2022/5/16 16:25
 */
class CollectAdapter(resId: Int, dataList: MutableList<CardFragment>) :
    BaseRecyclerViewAdapter<CardFragment, DataBindBaseViewHolder>(resId, dataList) {

    private var collectFgBinding: CollectItemFragmentBinding? = null

    @SuppressLint("SetTextI18n")
    override fun convert(viewHolder: DataBindBaseViewHolder?, data: CardFragment?, position: Int) {
        collectFgBinding = viewHolder?.getBinding() as CollectItemFragmentBinding
        try {
            GlideUtils.loadImageRoundCorner(
                mContext,
                data?.img,
                collectFgBinding?.image,
                RoundCornersTransform(
                    DensityUtils.dip2px(0f).toFloat(),
                    DensityUtils.dip2px(0f).toFloat(),
                    DensityUtils.dip2px(0f).toFloat(),
                    DensityUtils.dip2px(0f).toFloat()
                )
            )
            if (data?.holdNum!! >= data.needNum){
                collectFgBinding?.image?.alpha = 1f
            } else {
                collectFgBinding?.image?.alpha = 0.3f
            }
            collectFgBinding?.numTv?.text = "${data.holdNum}/${data.needNum}"
        } catch (e:Exception){}
    }

}