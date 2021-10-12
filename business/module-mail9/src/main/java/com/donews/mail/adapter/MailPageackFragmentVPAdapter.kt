package com.donews.mail.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.detail.R
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.views.MailPackFragmentVpLayout

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 包邮页面的
 */
class MailPageackFragmentVPAdapter : BaseQuickAdapter<MailPackageTabItem, BaseViewHolder>(
    R.layout.mail_package_fragment_vp_item
) {
    override fun createBaseViewHolder(view: View): BaseViewHolder {
        return super.createBaseViewHolder(view)
    }

    override fun convert(helper: BaseViewHolder, item: MailPackageTabItem?) {
        when (helper.itemView) {
            is MailPackFragmentVpLayout -> {
                //绑定数据
                (helper.itemView as MailPackFragmentVpLayout).bindDataList(item!!)
            }
        }
    }
}