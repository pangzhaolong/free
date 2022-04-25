package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.BuildConfig;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.home.FactorySaleBean;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 11:12<br>
 * 版本：V1.0<br>
 */
public class CrazyModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeGoodsBean> getRealTimeData(int pageId, String src) {
        MutableLiveData<HomeGoodsBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(String.format(HomeApi.perfectGoodsListUrl, 20, pageId, src))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeGoodsBean homeGoodsBean) {
                        mutableLiveData.postValue(homeGoodsBean);
                    }
                });

        return mutableLiveData;
    }


    public MutableLiveData<FactorySaleBean> getFactorySale(String page_id,String page_size,String url) {
        MutableLiveData<FactorySaleBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(url+ "?page_id="+page_id+"&page_size="+page_size)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<FactorySaleBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(FactorySaleBean secKilBean) {
                        mutableLiveData.postValue(secKilBean);
                    }
                }));

        return mutableLiveData;
    }
}
