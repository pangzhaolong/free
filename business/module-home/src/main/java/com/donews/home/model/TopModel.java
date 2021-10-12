package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.BuildConfig;
import com.donews.home.api.HomeApi;
import com.donews.home.bean.DataBean;
import com.donews.home.bean.TopGoodsBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 11:12<br>
 * 版本：V1.0<br>
 */
public class TopModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<DataBean> getNetData() {
        MutableLiveData<DataBean> mutableLiveData = new MutableLiveData<>();

        EasyHttp.get(HomeApi.TopBannerUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<DataBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(DataBean dataBean) {
                        mutableLiveData.postValue(dataBean);
                    }
                });

        return mutableLiveData;
    }

    public MutableLiveData<TopGoodsBean> getTopGoodsData() {
        MutableLiveData<TopGoodsBean> mutableLiveData = new MutableLiveData<>();

        EasyHttp.get(HomeApi.TopBannerUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<TopGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(TopGoodsBean dataBean) {
                        mutableLiveData.postValue(dataBean);
                    }
                });

        return mutableLiveData;
    }
}
