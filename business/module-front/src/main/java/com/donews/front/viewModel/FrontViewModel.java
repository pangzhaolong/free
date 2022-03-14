package com.donews.front.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.front.model.FrontModel;
import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.front.AwardBean;
import com.donews.middle.bean.front.DoubleRedPacketBean;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.bean.front.LotteryDetailBean;
import com.donews.middle.bean.front.LotteryOpenRecord;
import com.donews.middle.bean.front.RedPacketBean;
import com.donews.middle.bean.front.WinningRotationBean;
import com.donews.middle.bean.home.ServerTimeBean;

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

    public MutableLiveData<DoubleRedPacketBean> openRpData(String restId, String preId) {
        return mModel.openRpData(restId, preId);
    }

    public MutableLiveData<WinningRotationBean> getWinnerList() {
        return mModel.getWinnerList();
    }

    public MutableLiveData<LotteryOpenRecord> getLotteryPeriod() {
        return mModel.getLotteryPeriod();
    }

    public MutableLiveData<LotteryDetailBean> getLotteryDetail(int period) {
        return mModel.getLotteryDetail(period);
    }

    public MutableLiveData<ServerTimeBean> getServerTime() {
        return mModel.getServerTime();
    }
}
