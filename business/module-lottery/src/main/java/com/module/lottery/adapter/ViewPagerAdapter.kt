package com.module.lottery.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.module.lottery.utils.ImageUtils
import java.util.ArrayList

class ViewPagerAdapter : PagerAdapter() {

    fun getDataList(): List<ImageView>? {
        return dataList
    }

    fun setDataList(dataList: List<ImageView>) {
        this.dataList = dataList as ArrayList<ImageView>
    }

    var dataList = ArrayList<ImageView>()

    override fun getCount(): Int {
        return dataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(dataList[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
     var view=   dataList[position]
        if(view!=null){
            container.removeView(view)
        }
        ImageUtils.setImage(container.context, view, view.getTag().toString(), 10);
        container.addView(dataList[position], 0)
        return dataList[position]
    }
}