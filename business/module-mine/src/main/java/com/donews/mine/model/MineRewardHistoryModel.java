package com.donews.mine.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.bean.RewardHistoryBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 15:30<br>
 * 版本：V1.0<br>
 */
public class MineRewardHistoryModel extends BaseLiveDataModel {

    /**
     * 开奖历史数据
     */
    public MutableLiveData<RewardHistoryBean> getHistoryData() {
        MutableLiveData<RewardHistoryBean> liveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(MineHttpApi.lotteryRewardHistoryUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<RewardHistoryBean>() {
                    @Override
                    public void onError(ApiException e) {
                        liveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(RewardHistoryBean rewardHistoryBean) {

                        liveData.postValue(rewardHistoryBean);
                    }
                }));
        return liveData;
    }
}
