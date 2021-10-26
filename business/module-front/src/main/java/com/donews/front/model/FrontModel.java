package com.donews.front.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.front.api.FrontApi;
import com.donews.front.bean.AwardBean;
import com.donews.front.bean.FrontBean;
import com.donews.front.bean.LotteryCategoryBean;
import com.donews.front.bean.RedPacketBean;
import com.donews.front.bean.WalletBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

public class FrontModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 frontBean的数据
     */
    public MutableLiveData<LotteryCategoryBean> getNetData() {
        MutableLiveData<LotteryCategoryBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.lotteryCategoryUrl)
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
}
