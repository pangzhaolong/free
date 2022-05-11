package com.donews.lotterypage.viewmodel

import androidx.lifecycle.MutableLiveData
import com.doing.spike.bean.SpikeBean
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.lotterypage.base.LotteryPageBean
import com.donews.lotterypage.model.LotteryPageModel

class LotteryPageViewModel : BaseLiveDataViewModel<LotteryPageModel>() {

    var liveData = MutableLiveData<LotteryPageBean>();


    override fun createModel(): LotteryPageModel? {
        return LotteryPageModel()
    }


    fun requestInternetData() {
        mModel.getNetData(liveData)
    }

}