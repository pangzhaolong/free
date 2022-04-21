package com.donews.utilslibrary.sdk

import com.dnstatistics.sdk.agent.DonewsAgent
import com.donews.utilslibrary.utilktx.ApplicationInject


/**
 * Suuid相关
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 18:24
 */

fun getSuuid(): String {
    return DonewsAgent.obtainSuuid(ApplicationInject.getApplication())
}