package com.donews.mail.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.mail.entitys.resps.MailPackHomeListItemResp

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 包邮页面的列表的适配器
 */
class MailPageackFragmentListAdapter(layoutResId: Int) : BaseQuickAdapter<MailPackHomeListItemResp, BaseViewHolder>(
    layoutResId
) {
    override fun convert(helper: BaseViewHolder, item: MailPackHomeListItemResp?) {
    }
}