package com.donews.lotterypage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.common.router.RouterFragmentPath
import com.donews.lotterypage.base.BaseFragment
import com.module_lottery.R
import com.module_lottery.databinding.LotteryPageLayoutBinding



class LotteryPageFragment() : BaseFragment<LotteryPageLayoutBinding>() {
    override val layoutId: Int
        get() = R.layout.lottery_page_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}







