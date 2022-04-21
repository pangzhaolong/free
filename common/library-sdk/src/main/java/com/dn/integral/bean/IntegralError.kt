package com.dn.integral.bean


/**
 * 积分墙获取列表接口错误信息
 *
 * @author: cymbi
 * @data: 2021/12/27
 */
class IntegralError(private val code: Int = 0, private val message: String = "") {

    fun getErrorMessage(): String {
        return message
    }

    fun getErrorCode(): Int {
        return code
    }
}