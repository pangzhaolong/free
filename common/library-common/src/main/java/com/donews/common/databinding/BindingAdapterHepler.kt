package com.donews.common.databinding

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.donews.utilslibrary.utils.DensityUtils

/**
 * data binding BindingAdapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/22 13:51
 */

@BindingAdapter(value = ["url", "roundRadius", "defaultImage", "placeholder"], requireAll = false)
fun imageLoader(
    view: ImageView,
    url: String? = null,
    roundRadius: Float = 0f,
    placeHolderDrawable: Drawable? = null,
    defaultDrawable: Drawable? = null
) {
    url?.trim().let {
        if (roundRadius > 0) {
            val radius = DensityUtils.dip2px(roundRadius)
            Glide.with(view)
                .load(it)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(radius)))
                .placeholder(placeHolderDrawable)
                .error(defaultDrawable)
                .into(view)
        } else {
            Glide.with(view)
                .load(it)
                .placeholder(placeHolderDrawable)
                .error(defaultDrawable)
                .into(view)
        }
    }
}

@BindingAdapter("tint")
fun bindTintColor(view: ImageView, color: Int) {
    view.imageTintList = ColorStateList.valueOf(color)
}