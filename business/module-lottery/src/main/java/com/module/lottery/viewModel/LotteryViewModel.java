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
   private MutableLiveData<CommodityBean> mutableLiveData = new MutableLiveData<CommodityBean>();
    //猜你喜欢的数据
    private  MutableLiveData<MaylikeBean> mGuessLike = new MutableLiveData<MaylikeBean>();

    //参与人数
    private MutableLiveData<ParticipateBean> mParticipateBean = new MutableLiveData<ParticipateBean>();

    //抽奖码
    private  MutableLiveData<LotteryCodeBean> mLotteryCodeBean = new MutableLiveData<LotteryCodeBean>();


    //获取中奖人员列表
    private  MutableLiveData<WinLotteryBean> mWinLotteryBean = new MutableLiveData<WinLotteryBean>();


    //获取商品信息
    public void getNetLotteryData(String url, Map<String, String> params) {
        mModel.getNetCommodityData(mutableLiveData, url, params);
    }
    //向view层提供网络数据
    public void getGuessLikeData(String url, Map<String, String> params) {
        mModel.getNetData(mGuessLike, url, params);
    }
    //向view层提供网络数据
    public void getParticipateNumberData(String url, Map<String, String> params) {
        mModel.getNetParticipateData(mParticipateBean, url, params);
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
        return mutableLiveData;
    }

    public void setMutableLiveData(MutableLiveData<CommodityBean> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }

    public MutableLiveData<MaylikeBean> getmGuessLike() {
        return mGuessLike;
    }

    public void setmGuessLike(MutableLiveData<MaylikeBean> mGuessLike) {
        this.mGuessLike = mGuessLike;
    }

    public MutableLiveData<ParticipateBean> getmParticipateBean() {
        return mParticipateBean;
    }

    public void setmParticipateBean(MutableLiveData<ParticipateBean> mParticipateBean) {
        this.mParticipateBean = mParticipateBean;
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
