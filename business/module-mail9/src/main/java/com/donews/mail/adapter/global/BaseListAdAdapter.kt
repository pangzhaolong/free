package com.donews.mail.adapter.global

import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.common.contract.BaseCustomViewModel
import com.donews.detail.R
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.views.MailPackFragmentVpLayout

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 全局的。通用的给指定的列表加载数据的
 */
abstract class BaseListAdAdapter<T : BaseCustomViewModel, VH : BaseViewHolder>(layoutId: Int) :
    BaseQuickAdapter<T, VH>(layoutId) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}