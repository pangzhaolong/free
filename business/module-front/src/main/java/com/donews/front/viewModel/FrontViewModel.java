package com.donews.front.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.front.bean.FrontBean;
import com.donews.front.bean.LotteryCategoryBean;
import com.donews.front.bean.RedPacketBean;
import com.donews.front.bean.WalletBean;
import com.donews.front.model.FrontModel;

public class FrontViewModel extends BaseLiveDataViewModel<FrontModel> {

    @Override
    public FrontModel createModel() {
        return new FrontModel();
    }


    public MutableLiveData<LotteryCategoryBean> getNetData() {
        return mModel.getNetData();
    }

    public MutableLiveData<WalletBean> getRpData() {
        return mModel.getRpData();
    }

    public MutableLiveData<RedPacketBean> openRpData() {
        return mModel.openRpData();
    }
}
