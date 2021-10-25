package com.module.lottery.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.RaidersBean;
import com.module.lottery.model.LotteryModel;

import java.util.Map;

public class Item1FragmentViewModel extends BaseLiveDataViewModel<LotteryModel> {
    @Override
    public LotteryModel createModel() {
        return new LotteryModel();
    }

    public MutableLiveData<RaidersBean> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setMutableLiveData(MutableLiveData<RaidersBean> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }

    //首页数据
    private MutableLiveData<RaidersBean> mutableLiveData = new MutableLiveData<RaidersBean>();

    //向view层提供网络数据
    public void getData(String url, Map<String, String> params) {
        mModel.getRaidersData(mutableLiveData, url, params);
    }


}
