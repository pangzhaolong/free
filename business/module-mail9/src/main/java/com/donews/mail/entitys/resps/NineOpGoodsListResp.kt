package com.donews.mail.entitys.resps

import com.donews.common.contract.BaseCustomViewModel

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  9.9包邮页面列表返回数据实体
 */
class MailPackHomeListResp : BaseCustomViewModel() {
    var list: MutableList<MailPackHomeListItemResp> = mutableListOf()
}

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  9.9包邮页面列表的每项数据item实体
 */
class MailPackHomeListItemResp : BaseCustomViewModel() {

}