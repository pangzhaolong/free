package com.donews.mine.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

object GlideUtils {
    fun glideRectCircleOssWGif(
        context: Context?,
        imgurl: String,
        gifurl: String,
        bill: Float,
        width: Int,
        imgView: ImageView
    ) {
        val imgZoom = when {
            imgurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            imgurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val gifZoom = when {
            gifurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            gifurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val aWidth = if (width > ScreenUtils.getScreenWidth() * bill) {
            ScreenUtils.getScreenWidth() * bill.toInt()
        } else {
            width
        }

        val thumbnailRequest = Glide
            .with(context!!)
            .load("${imgurl}${imgZoom}${aWidth}")

        Glide.with(context)
//                .load("${gifurl}${gifZoom}${aWidth}")
            .load("${gifurl}")
            .transition(withCrossFade())
            .thumbnail(thumbnailRequest)
            .into(imgView)
    }

    fun glideRectCircleOssWGif(
        context: Context?,
        imgurl: String,
        gifurl: String,
        bill: Float,
        width: Int,
        imgView: ImageView,
        drawable: Int
    ) {
        val imgZoom = when {
            imgurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            imgurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val gifZoom = when {
            gifurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            gifurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val aWidth = if (width > ScreenUtils.getScreenWidth() * bill) {
            ScreenUtils.getScreenWidth() * bill.toInt()
        } else {
            width
        }

        val thumbnailRequest = Glide
            .with(context!!)
            .load("${imgurl}${imgZoom}${aWidth}")

        Glide.with(context)
//                .load("${gifurl}${gifZoom}${aWidth}")
            .load("${gifurl}")
            .transition(withCrossFade())
            .placeholder(drawable)
            .thumbnail(thumbnailRequest)
            .into(imgView)
    }

    fun glideRectCircleCenterOssWGif(
        context: Context?,
        imgurl: String,
        gifurl: String,
        bill: Float,
        width: Int,
        imgView: ImageView,
        drawable: Int
    ) {
        val imgZoom = when {
            imgurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            imgurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val gifZoom = when {
            gifurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            gifurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val aWidth = if (width > ScreenUtils.getScreenWidth() * bill) {
            ScreenUtils.getScreenWidth() * bill.toInt()
        } else {
            width
        }

        val thumbnailRequest = Glide
            .with(context!!)
            .load("${imgurl}${imgZoom}${aWidth}")

        Glide.with(context)
//                .load("${gifurl}${gifZoom}${aWidth}")
            .load("${gifurl}")
            .transition(withCrossFade())
            .placeholder(drawable)
            .thumbnail(thumbnailRequest)
            .into(imgView)
    }

    fun glideRectCircleCenterOssWGif(
        context: Context?,
        imgurl: String,
        gifurl: String,
        bill: Float,
        width: Int,
        imgView: ImageView,
        drawable: Drawable
    ) {
        val imgZoom = when {
            imgurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            imgurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val gifZoom = when {
            gifurl.contains("x-oss-process=image") -> "&resize,m_mfit,w_"
            gifurl.contains("x-oss-process=video") -> ",w_"
            else -> "?x-oss-process=image/resize,m_mfit,w_"
        }

        val aWidth = if (width > ScreenUtils.getScreenWidth() * bill) {
            ScreenUtils.getScreenWidth() * bill.toInt()
        } else {
            width
        }

        val thumbnailRequest = Glide
            .with(context!!)
            .load("${imgurl}${imgZoom}${aWidth}")

        Glide.with(context)
//                .load("${gifurl.toImageUrl()}${gifZoom}${aWidth}")
            .load("${gifurl}")
            .transition(withCrossFade())
            .placeholder(drawable)
            .thumbnail(thumbnailRequest)
            .into(imgView)
    }
}