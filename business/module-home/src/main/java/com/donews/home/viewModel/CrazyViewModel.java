package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.CrazyModel;
import com.donews.middle.bean.home.RealTimeBean;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class CrazyViewModel extends BaseLiveDataViewModel<CrazyModel> {

    @Override
    public CrazyModel createModel() {
        return new CrazyModel();
    }

    public MutableLiveData<RealTimeBean> getCrazyListData(int pageId) {
        return mModel.getRealTimeData(pageId);
    }
}
