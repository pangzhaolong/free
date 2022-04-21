package com.dn.sdk.bean

import org.json.JSONObject

/**
 * ecpm 上传的参数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/21 15:19
 */
class EcpmParam {

    private var mEcpm: String = ""
    private var mDesc: String = ""
    private val mExtra: MutableMap<String, Any> = mutableMapOf()

    fun setDesc(desc: String) {
        mDesc = desc
    }

    fun setEcpm(ecpm: String) {
        mEcpm = ecpm
    }

    fun addExtraParams(key: String, any: Any) {
        mExtra[key] = any
    }

    override fun toString(): String {
        return getParamsJson()
    }

    fun getParamsJson(): String {
        return try {
            val jsonObject = JSONObject()
            if (mEcpm.isBlank()) {
                jsonObject.put("ecpm_price", "0")
            } else {
                mEcpm= mEcpm.replace("[.](.*)".toRegex(), "")
                jsonObject.put("ecpm_price", mEcpm)
            }
            jsonObject.put("desc", mDesc)
            jsonObject.put("extra", JSONObject(mExtra.toMap()))
            jsonObject.toString()
        } catch (e: Exception) {
            ""
        }
    }
}