package com.module.lottery.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.donews.utilslibrary.utils.UrlUtils

class ImageUtils {


    companion object {
        @JvmStatic
        public fun setImage(context: Context, view: ImageView, src: String, roundingRadius: Int) {
            var src: String? = src
            src = UrlUtils.formatUrlPrefix(src)
            val roundedCorners = RoundedCorners(roundingRadius)
            val options = RequestOptions.bitmapTransform(roundedCorners)
            Glide.with(context).load(src).apply(options).into(view)
        }
    }
}