package com.donews.front.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.front.bean.FrontBean;
import com.donews.front.model.FrontModel;
import com.donews.front.model.GiftModel;

public class GiftViewModel extends BaseLiveDataViewModel<GiftModel> {

    @Override
    public GiftModel createModel() {
        return new GiftModel();
    }


    public MutableLiveData<FrontBean> getNetData() {
        return mModel.getNetData();
    }
}
