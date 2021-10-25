package com.module.lottery.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.ContactCustomerBean;
import com.module.lottery.model.LotteryModel;

import java.util.Map;

//联系客服页面
public class ContactCustomerViewModel extends BaseLiveDataViewModel<LotteryModel> {


    public MutableLiveData<ContactCustomerBean> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setMutableLiveData(MutableLiveData<ContactCustomerBean> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }

    //首页数据
    private MutableLiveData<ContactCustomerBean> mutableLiveData = new MutableLiveData<ContactCustomerBean>();


    @Override
    public LotteryModel createModel() {
        return new LotteryModel();
    }



    //获取商品信息
    public void getContactCustomerData(String url, Map<String, String> params) {
        mModel.getContactCustomerData(mutableLiveData, url, params);
    }



}
