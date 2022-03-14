package com.donews.pictures.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.donews.pictures.fragment.PhotoFragment

/**
 * 图片适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 19:48
 */
class BigImagePageAdapter(
    fm: FragmentManager,
    private val imageList: List<String>? = null
) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return imageList?.size ?: 0
    }

    override fun getItem(position: Int): Fragment {
        val url = imageList?.let {
            it[position]
        } ?: ""
        return PhotoFragment.newInstance(url)
    }
}