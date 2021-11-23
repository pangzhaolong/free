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

public class NorModel extends BaseLiveDataModel {

    /**
     * 获取网路数据
     *
     * @return 返回 frontBean的数据
     */
    @SuppressLint("DefaultLocale")
    public MutableLiveData<LotteryGoodsBean> getNetData(String categoryId, int pageId) {
        MutableLiveData<LotteryGoodsBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(String.format(FrontApi.lotteryGoodsUrl, categoryId, pageId))
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
