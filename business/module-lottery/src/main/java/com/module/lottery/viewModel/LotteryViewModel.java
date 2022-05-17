package com.module.lottery.viewModel;

import android.util.ArrayMap;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module.lottery.bean.ParticipateBean;
import com.module.lottery.bean.WinLotteryBean;
import com.module.lottery.model.LotteryModel;

import java.util.Map;

public class LotteryViewModel extends BaseLiveDataViewModel<LotteryModel> {
    //首页数据
    private MutableLiveData<CommodityBean> mutableLiveData_ = new MutableLiveData<CommodityBean>();


    //抽奖码
    private MutableLiveData<LotteryCodeBean> mLotteryCodeBean = new MutableLiveData<LotteryCodeBean>();




    //获取中奖人员列表
    private MutableLiveData<WinLotteryBean> mWinLotteryBean = new MutableLiveData<WinLotteryBean>();


    //获取商品信息
    public void getNetLotteryData(String url, Map<String, String> params) {
        mModel.getNetCommodityData(mutableLiveData_, url, params);
    }

    //向view层提供网络数据
    public void getLotteryCodeData(String url, Map<String, String> params) {
        mModel.getNetLotteryCodeData(mLotteryCodeBean, url, params);
    }


    //向view层提供查询中奖人员列表网络数据
    public void getWinLotteryList(String url, Map<String, String> params) {
        mModel.getWinLotteryList(mWinLotteryBean, url, params);
    }


    public MutableLiveData<WinLotteryBean> getWinLotteryBean() {
        return mWinLotteryBean;
    }

    public void setWinLotteryBean(MutableLiveData<WinLotteryBean> mWinLotteryBean) {
        this.mWinLotteryBean = mWinLotteryBean;
    }

    public MutableLiveData<CommodityBean> getMutableLiveData() {
        return mutableLiveData_;
    }

    public void setMutableLiveData(MutableLiveData<CommodityBean> mutableLiveData) {
        this.mutableLiveData_ = mutableLiveData;
    }



    public MutableLiveData<LotteryCodeBean> getmLotteryCodeBean() {
        return mLotteryCodeBean;
    }

    public void setmLotteryCodeBean(MutableLiveData<LotteryCodeBean> mLotteryCodeBean) {
        this.mLotteryCodeBean = mLotteryCodeBean;
    }


    @Override
    public LotteryModel createModel() {
        return new LotteryModel();
    }
}
