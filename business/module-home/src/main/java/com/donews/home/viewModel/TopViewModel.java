package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.bean.DataBean;
import com.donews.home.bean.RealTimeBean;
import com.donews.home.bean.SecKilBean;
import com.donews.home.bean.TopGoodsBean;
import com.donews.home.model.TopModel;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class TopViewModel extends BaseLiveDataViewModel<TopModel> {

    @Override
    public TopModel createModel() {
        return new TopModel();
    }


    public MutableLiveData<DataBean> getTopBannerData() {
        return mModel.getNetData();
    }

    public MutableLiveData<TopGoodsBean> getTopGoodsData(int pageId) {
        return mModel.getTopGoodsData(pageId);
    }

    public MutableLiveData<RealTimeBean> getRealTimeData() {
        return mModel.getRealTimeData();
    }

    public MutableLiveData<SecKilBean> getSecKilData() {
        return mModel.getSecKilData();
    }
}
