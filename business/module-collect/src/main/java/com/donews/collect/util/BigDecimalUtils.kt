package com.donews.collect.util

import java.math.BigDecimal

/**
 *  make in st
 *  on 2022/5/19 14:32
 */
object BigDecimalUtils {

    fun mul(v1: Int, v2: Int, scale: Int): String {
        return mul(v1.toString() + "", v2.toString() + "", scale)
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1    被加数
     * @param v2    加数
     * @param scale 保留scale 位小数
     * @return 两个参数的和
     */
    fun add(v1: String?, v2: String?, scale: Int): String {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1    被减数
     * @param v2    减数
     * @param scale 保留scale 位小数
     * @return 两个参数的差
     */
    fun sub(v1: String?, v2: String?, scale: Int): String {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1    被乘数
     * @param v2    乘数
     * @param scale 保留scale 位小数
     * @return 两个参数的积
     */
    fun mul(v1: String?, v2: String?, scale: Int): String {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    }

    fun div(v1: Int, v2: Int, scale: Int, isUp: Boolean): String {
        return div(v1.toString() + "", v2.toString() + "", scale, isUp)
    }

    /**
     * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位
     * @param isUp  是否是四舍五入，否则，去掉
     * @return 两个参数的商
     */
    fun div(v1: String?, v2: String?, scale: Int, isUp: Boolean): String {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return if (isUp) {
            val divide: BigDecimal = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
            divide.toString()
        } else {
            val divide: BigDecimal = b1.divide(b2, scale, BigDecimal.ROUND_DOWN)
            divide.toString()
        }
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    fun round(v: Double, scale: Int): Double {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b = BigDecimal(v.toString())
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    fun round(v: String?, scale: Int): String {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b = BigDecimal(v)
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    }

    /**
     * 取余数
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 小数点后保留几位
     * @return 余数
     */
    fun remainder(v1: String?, v2: String?, scale: Int): String {
        var scale = scale
        if (scale < 0) {
            scale = 0
        }
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        return b1.remainder(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    }

    /**
     * 比较大小
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 大于v2 则 返回true 否则false
     */
    fun compare(v1: String?, v2: String?): Boolean {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        val bj: Int = b1.compareTo(b2)
        return bj > 0
    }

}