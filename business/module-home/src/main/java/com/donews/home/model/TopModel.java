package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.fh.HomeDataBean;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SecKilBean;
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
public class TopModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeDataBean> getNetData() {
        MutableLiveData<HomeDataBean> mutableLiveData = new MutableLiveData<>();

        EasyHttp.get(HomeApi.TopBannerUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeDataBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        mutableLiveData.postValue(homeDataBean);
                    }
                });

        return mutableLiveData;
    }

    public MutableLiveData<HomeGoodsBean> getTopGoodsData(int pageId) {
        MutableLiveData<HomeGoodsBean> mutableLiveData = new MutableLiveData<>();

        EasyHttp.get(HomeApi.goodsList + "?page_size=20&page_id=" + pageId)
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

    public MutableLiveData<RealTimeBean> getRealTimeData() {
        MutableLiveData<RealTimeBean> mutableLiveData = new MutableLiveData<>();

        EasyHttp.get(HomeApi.realTimeUrl + "?rank_type=1&page_size=2")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<RealTimeBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(RealTimeBean realTimeBean) {
                        mutableLiveData.postValue(realTimeBean);
                    }
                });

        return mutableLiveData;
    }

    public MutableLiveData<SecKilBean> getSecKilData() {
        MutableLiveData<SecKilBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.seckiltUrl + "?&page_size=2")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<SecKilBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(SecKilBean secKilBean) {
                        mutableLiveData.postValue(secKilBean);
                    }
                });

        return mutableLiveData;
    }
}
