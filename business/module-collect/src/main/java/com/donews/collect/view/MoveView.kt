package com.donews.collect.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.donews.base.utils.glide.GlideUtils
import com.donews.collect.R
import com.donews.collect.bean.DanMuBean
import com.donews.common.views.CircleImageView
import com.donews.utilslibrary.utils.DensityUtils

/**
 *  make in st
 *  on 2021/12/6 13:49
 */
@SuppressLint("ViewConstructor")
class MoveView constructor(context: Context, danMuBean: DanMuBean) : LinearLayout(context) {

    private lateinit var stBgView01: View
    private lateinit var stBgView02: View
    private lateinit var stImage: CircleImageView
    private lateinit var name: TextView
    private lateinit var doWhat: TextView
    private lateinit var goodName: TextView

    init {
        addView(View.inflate(context, R.layout.collect_item_danmu, null))
        initView(danMuBean)
    }

    private fun initView(danMuBean: DanMuBean) {
        stBgView01 = rootView!!.findViewById(R.id.stBgView01)
        stBgView02 = rootView!!.findViewById(R.id.stBgView02)
        stImage = rootView!!.findViewById(R.id.stImage)
        name = rootView!!.findViewById(R.id.name)
        doWhat = rootView!!.findViewById(R.id.doWhat)
        goodName = rootView!!.findViewById(R.id.goodName)
        if (danMuBean.avatar == "") {
            stImage.visibility = GONE
        } else {
            stImage.visibility = VISIBLE
            GlideUtils.loadImageView(context, danMuBean.avatar, stImage)
        }
        name.text = danMuBean.name
        goodName.text = danMuBean.goods_name
        val params = stBgView01.layoutParams as RelativeLayout.LayoutParams
        val w1 = name.paint.measureText(name.text.toString()).toInt()
        val w2 = doWhat.paint.measureText(doWhat.text.toString()).toInt()
        val w3 = goodName.paint.measureText(goodName.text.toString()).toInt()
        params.width = w1 + w2 + w3 + DensityUtils.dip2px(40f)
        stBgView01.visibility = View.VISIBLE
        stBgView01.layoutParams = params
        stBgView02.visibility = View.GONE
    }

    /**
     * 设置随机出现垂直位置
     */
    fun randomVerticalPos() {
        val params = layoutParams as RelativeLayout.LayoutParams
        params.topMargin = (Math.random() * DensityUtils.dip2px(30f)).toInt()
        layoutParams = params
    }

}