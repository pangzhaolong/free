package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.BuildConfig;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchRespBean;
import com.donews.middle.bean.home.SearchSugBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 11:12<br>
 * 版本：V1.0<br>
 */
public class SearchModel extends BaseLiveDataModel {


    /**
     * 获取新的搜索数据
     *
     * @param key_words 搜索内容
     * @param page_id   页码
     * @param page_size 分页大小
     * @return
     */
    public MutableLiveData<SearchRespBean> getSearchNewData(String key_words, String page_id, int page_size) {
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/goods-search");
        MutableLiveData<SearchRespBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .params("key_words", key_words)
                .params("page_id", page_id)
                .params("page_size", page_size + "")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<SearchRespBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(SearchRespBean searchSugBean) {
                        mutableLiveData.postValue(searchSugBean);
                    }
                });

        return mutableLiveData;
    }

    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<SearchSugBean> getSearchData(String search) {
        MutableLiveData<SearchSugBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(String.format(HomeApi.searchSugUrl, search))
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

    public MutableLiveData<HomeGoodsBean> getBuysData(int pageId) {
        MutableLiveData<HomeGoodsBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.goodsListUrl + "?page_size=20&page_id=" + pageId)
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
