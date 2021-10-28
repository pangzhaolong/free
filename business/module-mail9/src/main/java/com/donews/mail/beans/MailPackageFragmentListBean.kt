package com.donews.mail.beans

import com.donews.common.contract.BaseCustomViewModel
import com.donews.mail.entitys.resps.MailPackHomeListItemResp

/**
 * @author lcl
 * Date on 2021/10/14
 * Description:
 *  用于存放的数据通知的业务实现类
 */
class MailPackageFragmentListBean(
    /** 当前更新的类型 */
    val tabItem: MailPackageTabItem,
    val updateList:MutableList<MailPackHomeListItemResp>?
): BaseCustomViewModel() {
}