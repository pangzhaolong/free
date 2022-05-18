package com.donews.lotterypage.viewmodel

import androidx.lifecycle.MutableLiveData
import com.doing.spike.bean.SpikeBean
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.lotterypage.base.LotteryPageBean
import com.donews.lotterypage.base.LotteryPastBean
import com.donews.lotterypage.model.LotteryPageModel

class LotteryPageViewModel : BaseLiveDataViewModel<LotteryPageModel>() {

    var liveData = MutableLiveData<LotteryPageBean>();

    var livePastData = MutableLiveData<LotteryPastBean>();


    override fun createModel(): LotteryPageModel? {
        return LotteryPageModel()
    }


    fun requestInternetData() {
        //获取商品数据
        mModel.getNetData(liveData)
        //获取往期开奖数据
        mModel.getPastNetData(livePastData)

    }

}