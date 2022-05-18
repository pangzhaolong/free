package com.donews.lotterypage.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.doing.spike.adapter.SpikeContextAdapter
import com.doing.spike.bean.CombinationSpikeBean
import com.doing.spike.bean.SpikeBean
import com.doing.spike.bean.SpikeBean.GoodsListDTO
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterActivityPath
import com.donews.common.router.RouterFragmentPath
import com.donews.lotterypage.Adapter.ContentAdapter
import com.donews.lotterypage.R
import com.donews.lotterypage.base.BaseFragment
import com.donews.lotterypage.base.LotteryPageBean
import com.donews.lotterypage.databinding.LotteryPageLayoutBinding
import com.donews.lotterypage.viewmodel.LotteryPageViewModel
import com.donews.middle.front.FrontConfigManager
import com.donews.middle.front.LotteryConfigManager
import com.donews.middle.views.TaskView

@Route(path = RouterFragmentPath.HomeLottery.PAGER_LOTTERY)
class LotteryPageFragment :
    MvvmLazyLiveDataFragment<LotteryPageLayoutBinding, LotteryPageViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.lottery_page_layout
    }

    var mContentAdapter: ContentAdapter? = null;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         //运营位
        if (LotteryConfigManager.Ins().getConfigBean().getTask()) {
            mDataBinding.advertise.setVisibility(View.VISIBLE);
            mDataBinding.advertise.refreshYyw(TaskView.Place_Front);
        } else {
            mDataBinding.advertise.setVisibility(View.GONE);
        }

        mContentAdapter = ContentAdapter(this.requireContext());
        mContentAdapter?.getLayout(R.layout.lottery_page_content_item)
        mDataBinding.spikeContentRecyclerView.layoutManager = LinearLayoutManager(
            context
        )
        mDataBinding.spikeContentRecyclerView.adapter = mContentAdapter
        mContentAdapter?.setOnItemClickListener(object : ContentAdapter.OnItemClickListener {
            override fun onItemClick(goodsListDTO: LotteryPageBean.ListDTO?) {
                if (goodsListDTO != null) {
                    ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", goodsListDTO.goodsId)
                        .navigation()
                }
            }
        })

        dataObservation()
    }

    override fun onResume() {
        super.onResume()
        initContent()
    }

    //观察请求回来的数据
    fun dataObservation() {
        mViewModel.liveData.observe(viewLifecycleOwner, object : Observer<LotteryPageBean?> {
            override fun onChanged(t: LotteryPageBean?) {
                if (t != null) {
                    mContentAdapter?.setLotteryPageBean(t)
                    mContentAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun initContent() {
        mViewModel.requestInternetData()
    }


}







