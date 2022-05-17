package com.donews.collect.adapter

import android.view.View
import com.donews.base.utils.glide.GlideUtils
import com.donews.base.utils.glide.RoundCornersTransform
import com.donews.collect.bean.GoodBean
import com.donews.collect.databinding.CollectItemGoodBinding
import com.donews.library_recyclerview.BaseRecyclerViewAdapter
import com.donews.library_recyclerview.DataBindBaseViewHolder
import com.donews.utilslibrary.utils.DensityUtils

/**
 *  make in st
 *  on 2022/5/16 16:25
 */
class GoodAdapter(resId: Int, dataList: MutableList<GoodBean>) :
    BaseRecyclerViewAdapter<GoodBean, DataBindBaseViewHolder>(resId, dataList) {

    private var collectBinding: CollectItemGoodBinding? = null
    var clickCall: (position:Int) ->Unit = {}

    override fun convert(viewHolder: DataBindBaseViewHolder?, data: GoodBean?, position: Int) {
        collectBinding = viewHolder?.getBinding() as CollectItemGoodBinding
        if (data?.mainPic != null && data.mainPic != ""){
            GlideUtils.loadImageRoundCorner(mContext,data.mainPic,collectBinding?.image,
                RoundCornersTransform(DensityUtils.dip2px(10f).toFloat(),
                    DensityUtils.dip2px(10f).toFloat(),
                    DensityUtils.dip2px(10f).toFloat(),
                    DensityUtils.dip2px(10f).toFloat())
            )
        }
        collectBinding?.tv?.text = data?.title
        collectBinding?.selectBg?.visibility = View.GONE
        collectBinding?.selectIcon?.visibility = View.GONE
        data?.let {
            if (it.isSelect){
                collectBinding?.selectBg?.visibility = View.VISIBLE
                collectBinding?.selectIcon?.visibility = View.VISIBLE
            }
        }
        setOnItemClickListener(object: OnItemClickListener{
            override fun onItemClick(
                view: View?,
                viewHolder: DataBindBaseViewHolder?,
                position: Int
            ) {
                clickCall.invoke(position)
            }

            override fun onItemLongClick(
                view: View?,
                viewHolder: DataBindBaseViewHolder?,
                position: Int
            ): Boolean {
                return false
            }

        })
    }

}