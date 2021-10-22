package com.donews.front.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.front.bean.FrontBean;
import com.donews.front.bean.NorGoodsBean;
import com.donews.front.model.GiftModel;
import com.donews.front.model.NorModel;

public class NorViewModel extends BaseLiveDataViewModel<NorModel> {

    @Override
    public NorModel createModel() {
        return new NorModel();
    }


    public MutableLiveData<NorGoodsBean> getNetData(String categoryId, String pageId) {
        return mModel.getNetData(categoryId, pageId);
    }
}
