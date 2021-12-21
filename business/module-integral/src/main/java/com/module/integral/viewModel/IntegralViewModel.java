package com.module.integral.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.integral.model.IntegralModel;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module.lottery.bean.ParticipateBean;
import com.module.lottery.bean.WinLotteryBean;
import com.module.lottery.model.LotteryModel;

import java.util.Map;

public class IntegralViewModel extends BaseLiveDataViewModel<IntegralModel> {
   private MutableLiveData<CommodityBean> mutableLiveData = new MutableLiveData<CommodityBean>();

    //获取商品信息
    public void getNetLotteryData(String url, Map<String, String> params) {
//        mModel.
    }

    @Override
    public IntegralModel createModel() {
        return new IntegralModel();
    }
}
