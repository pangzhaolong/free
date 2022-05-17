package com.donews.turntable.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.donews.turntable.bean.RewardedBean;

public class GetRewardedViewModel extends ViewModel {
    MutableLiveData<RewardedBean> rewardedBean = new MutableLiveData<>();
}
