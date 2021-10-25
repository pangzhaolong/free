package com.module.lottery.viewModel;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.lottery.model.LotteryModel;

public class RaidersViewModel extends BaseLiveDataViewModel<LotteryModel> {
    @Override
    public LotteryModel createModel() {
        return new LotteryModel();
    }
}
