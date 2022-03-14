package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.NorModel;
import com.donews.middle.bean.front.LotteryGoodsBean;
import com.donews.middle.bean.home.HomeGoodsBean;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class NorViewModel extends BaseLiveDataViewModel<NorModel>{

    @Override
    public NorModel createModel() {
        return new NorModel();
    }


    public MutableLiveData<HomeGoodsBean> getNorGoodsData(String cids, int pageId){
        return mModel.getNorGoodsData(cids, pageId);
    }
 }
