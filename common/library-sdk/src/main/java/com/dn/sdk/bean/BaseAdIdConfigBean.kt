package com.dn.sdk.bean

/**
 * 广告配置相关接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:02
 */
abstract class BaseAdIdConfigBean {

    /** 通过key 返回对应的广告id */
    abstract fun getAdIdByKey(key: String): String
}