package com.donews.front.model;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.front.api.FrontApi;
import com.donews.middle.bean.front.LotteryGoodsBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

public class NorModel extends BaseLiveDataModel {

    /**
     * 获取网路数据
     *
     * @return 返回 frontBean的数据
     */
    @SuppressLint("DefaultLocale")
    public MutableLiveData<LotteryGoodsBean> getNetData(String categoryId, int pageId) {
        MutableLiveData<LotteryGoodsBean> mutableLiveData = new MutableLiveData<>();
        int nInAPpCount = SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP, 0);

        String url = String.format(FrontApi.lotteryGoodsUrl, categoryId, pageId) + "&first=true";
        if (nInAPpCount == 1) {
            url = String.format(FrontApi.lotteryGoodsUrl, categoryId, pageId) + "&first=true";
        } else {
            url = String.format(FrontApi.lotteryGoodsUrl, categoryId, pageId) + "&first=false";
        }

        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<LotteryGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(LotteryGoodsBean lotteryGoodsBean) {
                        mutableLiveData.postValue(lotteryGoodsBean);
                    }
                }));

        return mutableLiveData;
    }
}
