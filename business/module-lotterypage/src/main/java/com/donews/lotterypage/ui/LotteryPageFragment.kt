package com.donews.lotterypage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.lotterypage.base.BaseFragment
import com.donews.lotterypage.viewmodel.LotteryPageViewModel
import com.module_lottery.R
import com.module_lottery.databinding.LotteryPageLayoutBinding
import com.module_lottery.databinding.TestLayoutBinding

@Route(path = RouterFragmentPath.HomeLottery.PAGER_LOTTERY)
class LotteryPageFragment : MvvmLazyLiveDataFragment<TestLayoutBinding, LotteryPageViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.test_layout
    }


}







