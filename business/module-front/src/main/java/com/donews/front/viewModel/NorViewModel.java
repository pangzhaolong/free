package com.donews.front.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.front.model.NorModel;
import com.donews.middle.bean.front.LotteryGoodsBean;

public class NorViewModel extends BaseLiveDataViewModel<NorModel> {

    @Override
    public NorModel createModel() {
        return new NorModel();
    }


    public MutableLiveData<LotteryGoodsBean> getNetData(String categoryId, int pageId) {
        return mModel.getNetData(categoryId, pageId);
    }
}
