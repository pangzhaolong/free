package com.donews.unboxing.databinding

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.databinding.BindingConversion
import java.text.SimpleDateFormat
import java.util.*

/**
 * data binding converter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/22 13:44
 */

/** 时间戳转为时间字符串 */
@BindingConversion
fun convertData(dataTime: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val data = Date(dataTime)
    return sdf.format(data)
}