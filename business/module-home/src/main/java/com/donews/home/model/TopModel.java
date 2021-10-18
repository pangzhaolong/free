package com.donews.home.model;

import android.os.SystemClock;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.BuildConfig;
import com.donews.home.api.HomeApi;
import com.donews.home.bean.DataBean;
import com.donews.home.bean.RealTimeBean;
import com.donews.home.bean.SecKilBean;
import com.donews.home.bean.TopGoodsBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.DateTimeUtils;

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

        EasyHttp.get(HomeApi.goodsList)
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
//        String roundTime = DateTimeUtils.getNow("yyyy-MM-dd HH:00:00");
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
