package com.donews.home.model;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchSugBean;
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
public class WelfareModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    @SuppressLint("DefaultLocale")
    public MutableLiveData<HomeGoodsBean> getPerfectGoodsData(String from, int pageId) {
        int src = 1;
        if (from.equalsIgnoreCase("tb")) {
            src = 1;
        } else if (from.equalsIgnoreCase("pdd")) {
            src = 2;
        } else if (from.equalsIgnoreCase("jd")) {
            src = 3;
        }

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

    public MutableLiveData<SearchSugBean> getSearchData(String keyWord) {

        MutableLiveData<SearchSugBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(String.format(HomeApi.searchSugUrl, keyWord))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<SearchSugBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(SearchSugBean searchSugBean) {
                        mutableLiveData.postValue(searchSugBean);
                    }
                });

        return mutableLiveData;
    }

    @SuppressLint("DefaultLocale")
    public MutableLiveData<HomeGoodsBean> getSearchListData(int pageId, String keyWord, int src) {

        MutableLiveData<HomeGoodsBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(String.format(HomeApi.searchGoodsListUrl, pageId, keyWord, src))
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


}
