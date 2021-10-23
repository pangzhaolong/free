package com.donews.mine.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.bean.RewardHistoryBean;
import com.donews.mine.model.MineRewardHistoryModel;

public class MineRewardHistoryViewModel extends BaseLiveDataViewModel<MineRewardHistoryModel> {

    @Override
    public MineRewardHistoryModel createModel() {
        return new MineRewardHistoryModel();
    }


    public MutableLiveData<RewardHistoryBean> getHistoryData() {
        return mModel.getHistoryData();
    }
}
