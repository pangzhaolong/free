package com.donews.mail.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.base.utils.glide.GlideUtils
import com.donews.common.router.RouterActivityPath
import com.donews.detail.R
import com.donews.mail.adapter.global.BaseListAdAdapter
import com.donews.mail.entitys.resps.MailPackHomeListItemResp

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 包邮页面的列表的适配器
 */
class MailPageackFragmentListAdapter : BaseListAdAdapter<MailPackHomeListItemResp, BaseViewHolder>(
    R.layout.mail_package_vp_list_item
), LoadMoreModule {
    override fun convert(helper: BaseViewHolder, item: MailPackHomeListItemResp?) {
        item?.apply {
            //绑定文字数据
            helper.setText(R.id.vp_list_title, item.title)
                .setText(R.id.vp_list_price, "${item.originalPrice ?: 0 - item.couponPrice!!}")
                .setText(
                    R.id.vp_list_flg_price, "${
                        item.couponPrice?.let {
                            if (it - it.toInt() == 0F) {
                                it.toInt()
                            } else {
                                it
                            }
                        } ?: 0
                    }元"
                )
                .setText(
                    R.id.vp_list_count,
                    getSales(item)
                )
            //图标
            GlideUtils.loadImageView(
                helper.itemView.context,
                item.mainPic,
                helper.getView(R.id.vp_list_icon)
            )
            helper.itemView.setOnClickListener {
                //点击
                ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                    .withString("params_id", id)
                    .withString("params_goods_id", goodsId)
                    .navigation()
            }
        }
    }

    //获取销量
    private fun getSales(item: MailPackHomeListItemResp): String {
        var mothSalesDw = ""
        var daySalesDw = ""
        var monthSales = item.monthSales?.let {
            if (it > 10000) {
                mothSalesDw = "万"
                (it / 10000)
            } else {
                it
            }
        } ?: 0
        var dailySales = item.dailySales?.let {
            if (it > 10000) {
                daySalesDw = "万"
                (it / 10000)
            } else {
                it
            }
        } ?: 0
        return "销量${monthSales}$mothSalesDw | 日销${dailySales}$daySalesDw"
    }
}