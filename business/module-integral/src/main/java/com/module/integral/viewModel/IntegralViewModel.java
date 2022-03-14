package com.module.integral.viewModel;


import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.integral.bean.IntegralDownloadStateDean;
import com.module.integral.model.IntegralModel;

import java.util.Map;

public class IntegralViewModel extends BaseLiveDataViewModel<IntegralModel> {
    public MutableLiveData<IntegralDownloadStateDean> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setMutableLiveData(MutableLiveData<IntegralDownloadStateDean> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }

    private MutableLiveData<IntegralDownloadStateDean> mutableLiveData = new MutableLiveData<IntegralDownloadStateDean>();

    //获取商品信息
    public void getDownloadStatus(String url, Map<String, String> params) {
        mModel.getDownloadStatus(mutableLiveData,url,params);
    }

    @Override
    public IntegralModel createModel() {
        return new IntegralModel();
    }
}
