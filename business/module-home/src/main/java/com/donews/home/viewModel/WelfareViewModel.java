package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.CrazyModel;
import com.donews.home.model.WelfareModel;
import com.donews.middle.bean.home.PerfectGoodsBean;
import com.donews.middle.bean.home.RealTimeBean;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class WelfareViewModel extends BaseLiveDataViewModel<WelfareModel> {

    @Override
    public WelfareModel createModel() {
        return new WelfareModel();
    }

    public MutableLiveData<PerfectGoodsBean> getPerfectGoodsData(String from, int pageId) {
        return mModel.getPerfectGoodsData(from, pageId);
    }
}
