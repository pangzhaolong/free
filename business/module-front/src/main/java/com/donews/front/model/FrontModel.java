package com.donews.front.model;

import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.front.api.FrontApi;
import com.donews.middle.bean.RestIdBean;
import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.front.AwardBean;
import com.donews.middle.bean.front.DoubleRedPacketBean;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.bean.front.LotteryDetailBean;
import com.donews.middle.bean.front.LotteryOpenRecord;
import com.donews.middle.bean.front.WinningRotationBean;
import com.donews.middle.bean.home.ServerTimeBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

import cn.cd.dn.sdk.models.utils.DNServiceTimeManager;
import io.reactivex.Observable;

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
                .isShowToast(false)
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

    public MutableLiveData<DoubleRedPacketBean> openRpData(String restId, String preId) {
        MutableLiveData<DoubleRedPacketBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.post(FrontApi.walletOpenRedPacketUrl_New)
                .upObject(new RestIdBean(restId, preId))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<DoubleRedPacketBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(DoubleRedPacketBean bean) {
                        mutableLiveData.postValue(bean);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<WinningRotationBean> getWinnerList() {
        MutableLiveData<WinningRotationBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.winningRotationUrl)
                .params("type", "0")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<WinningRotationBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(WinningRotationBean awardBean) {
                        mutableLiveData.postValue(awardBean);
                    }
                }));

        return mutableLiveData;
    }

/*    public MutableLiveData<AwardBean> getWinnerList() {
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
    }*/

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

    private Handler mHandl = new Handler();

    public MutableLiveData<ServerTimeBean> getServerTime() {
        MutableLiveData<ServerTimeBean> mutableLiveData = new MutableLiveData<>();
        long time = DNServiceTimeManager.Companion.getIns().getServiceTime();
        mHandl.postDelayed(() -> {
            if (time <= 0) {
                mutableLiveData.postValue(null);
            } else {
                long curTime = time / 1000;
                ServerTimeBean item = new ServerTimeBean();
                item.setNow("" + curTime);
                mutableLiveData.postValue(item);
            }
        }, 1000);
//        addDisposable(EasyHttp.get(FrontApi.serverTimeUrl)
//                .cacheMode(CacheMode.NO_CACHE)
//                .execute(new SimpleCallBack<ServerTimeBean>() {
//
//                    @Override
//                    public void onError(ApiException e) {
//                        mutableLiveData.postValue(null);
//                    }
//
//                    @Override
//                    public void onSuccess(ServerTimeBean serverTimeBean) {
//                        mutableLiveData.postValue(serverTimeBean);
//                    }
//                }));

        return mutableLiveData;
    }
}
