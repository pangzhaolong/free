package com.donews.lotterypage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.lotterypage.R
import com.donews.lotterypage.base.BaseFragment
import com.donews.lotterypage.databinding.LotteryPageLayoutBinding
import com.donews.lotterypage.databinding.TestLayoutBinding
import com.donews.lotterypage.viewmodel.LotteryPageViewModel

@Route(path = RouterFragmentPath.HomeLottery.PAGER_LOTTERY)
class LotteryPageFragment : MvvmLazyLiveDataFragment<LotteryPageLayoutBinding, LotteryPageViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.lottery_page_layout
    }


}







