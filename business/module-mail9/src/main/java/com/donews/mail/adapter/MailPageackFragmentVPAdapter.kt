package com.donews.mail.adapter

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.mail.R
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.views.MailPackFragmentVpLayout

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 包邮页面的
 */
class MailPageackFragmentVPAdapter(
    val targetVp: ViewPager2
) : BaseQuickAdapter<MailPackageTabItem, BaseViewHolder>(
    R.layout.mail_package_fragment_vp_item
) {
    override fun createBaseViewHolder(view: View): BaseViewHolder {
        return super.createBaseViewHolder(view)
    }

    override fun convert(holder: BaseViewHolder, item: MailPackageTabItem) {
        when (holder.itemView) {
            is MailPackFragmentVpLayout -> {
                //绑定数据
                (holder.itemView as MailPackFragmentVpLayout).bindDataList(
                    targetVp.currentItem == holder.adapterPosition,
                    item!!
                )
            }
        }
    }
}