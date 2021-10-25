package com.dn.sdk.sdk.bean

/**
 * 广告配置相关接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:02
 */
interface IAdIdConfigBean {

    /** 通过key 返回对应的广告id */
    fun getAdIdByKey(key: String): String

}