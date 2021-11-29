package com.donews.front.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.front.api.FrontApi;
import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.front.AwardBean;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.bean.front.LotteryDetailBean;
import com.donews.middle.bean.front.LotteryOpenRecord;
import com.donews.middle.bean.front.RedPacketBean;
import com.donews.middle.bean.home.ServerTimeBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

public class FrontModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 frontBean的数据
     */
    public MutableLiveData<LotteryCategoryBean> getNetData() {
        MutableLiveData<LotteryCategoryBean> mutableLiveData = new MutableLiveData<>();

        int nInAPpCount = SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP, 0);

        String url = HttpConfigUtilsKt.withConfigParams(FrontApi.lotteryCategoryUrl, true) + "&first=true";
        if (nInAPpCount == 1) {
            url = HttpConfigUtilsKt.withConfigParams(FrontApi.lotteryCategoryUrl, true) + "&first=true";
        } else {
            url = HttpConfigUtilsKt.withConfigParams(FrontApi.lotteryCategoryUrl, true) + "&first=false";
        }
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<LotteryCategoryBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(LotteryCategoryBean categoryBean) {
                        mutableLiveData.postValue(categoryBean);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<WalletBean> getRpData() {
        MutableLiveData<WalletBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.walletRedPacketUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<WalletBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(WalletBean walletBean) {
                        mutableLiveData.postValue(walletBean);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<RedPacketBean> openRpData() {
        MutableLiveData<RedPacketBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.post(FrontApi.walletRedPacketUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<RedPacketBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(RedPacketBean redPacketBean) {
                        mutableLiveData.postValue(redPacketBean);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<AwardBean> getAwardList() {
        MutableLiveData<AwardBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.awardListUrl + "?offset=1&limit=10")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<AwardBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(AwardBean awardBean) {
                        mutableLiveData.postValue(awardBean);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<LotteryOpenRecord> getLotteryPeriod() {
        MutableLiveData<LotteryOpenRecord> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.lotteryRecordUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<LotteryOpenRecord>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(LotteryOpenRecord lotteryOpenRecord) {
                        mutableLiveData.postValue(lotteryOpenRecord);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<LotteryDetailBean> getLotteryDetail(int period) {
        MutableLiveData<LotteryDetailBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.lotteryDetailUrl + "?period=" + period)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<LotteryDetailBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(LotteryDetailBean lotteryOpenRecord) {
                        mutableLiveData.postValue(lotteryOpenRecord);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<ServerTimeBean> getServerTime() {
        MutableLiveData<ServerTimeBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.serverTimeUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ServerTimeBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(ServerTimeBean serverTimeBean) {
                        mutableLiveData.postValue(serverTimeBean);
                    }
                }));

        return mutableLiveData;
    }
}
