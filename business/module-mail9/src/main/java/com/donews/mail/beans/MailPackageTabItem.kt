package com.donews.mail.beans

import com.donews.common.contract.BaseCustomViewModel

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 包邮Fragment的tab类型数据
 */
class MailPackageTabItem(
    /** -1-精选，1 -5.9元区，2 -9.9元区，3 -19.9元区（调整字段 */
    val type: Int,
    /** 显示的名称 */
    val name: String
) : BaseCustomViewModel()