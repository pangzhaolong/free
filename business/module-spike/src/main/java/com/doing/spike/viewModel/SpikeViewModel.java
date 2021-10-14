/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */


package com.doing.spike.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.doing.spike.bean.SpikeBean;
import com.doing.spike.model.SpikeModel;
import com.donews.base.viewmodel.BaseLiveDataViewModel;


public class SpikeViewModel extends BaseLiveDataViewModel<SpikeModel>{
    MutableLiveData<SpikeBean> mutableLiveData;
    @Override
    public SpikeModel createModel() {
        return new SpikeModel();
    }

    public MutableLiveData<SpikeBean> getNetHomeData(String time){
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        mModel.getNetData(mutableLiveData,time);
        return mutableLiveData;
    }
 }
