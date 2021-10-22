package com.donews.unboxing.adapter

import android.database.DatabaseUtils
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.unboxing.databinding.UnboxingItemPicBinding

/**
 *
 * 图片适配器
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/22 15:25
 */
class UnboxingPicAdapter(layoutResId: Int) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        DataBindingUtil.bind<UnboxingItemPicBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        val dataBinding = helper.getBinding<UnboxingItemPicBinding>()
        dataBinding?.url = item
    }

}