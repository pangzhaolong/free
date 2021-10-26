package com.module.lottery.viewModel;


import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.model.LotteryModel;

import java.util.Map;

//执行抽奖
public class ExecuteLotteryViewModel extends BaseLiveDataViewModel<LotteryModel> {
    public MutableLiveData<GenerateCodeBean> getExecuteLotteryData() {
        return mExecuteLotteryData;
    }

    public void setExecuteLotteryData(MutableLiveData<GenerateCodeBean> executeLotteryData) {
        this.mExecuteLotteryData = executeLotteryData;
    }

    MutableLiveData<GenerateCodeBean> mExecuteLotteryData=new MutableLiveData<GenerateCodeBean>();

    @Override
    public LotteryModel createModel() {
        return new LotteryModel();
    }

    //向view层提供网络数据 生成抽奖码
    public void getExecuteLottery(String url, Map<String, String> params) {
        mModel.getGenerateCode(mExecuteLotteryData, url, params);
    }




}
