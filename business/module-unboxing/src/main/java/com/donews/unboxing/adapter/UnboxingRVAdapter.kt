package com.donews.unboxing.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.unboxing.bean.UnboxingBean

/**
 * 晒单页RecyclerView Adapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 19:21
 */
class UnboxingRVAdapter(layoutResId: Int) : BaseQuickAdapter<UnboxingBean, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: UnboxingBean?) {

    }
}