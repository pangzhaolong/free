package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.home.GoodsListBean;
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
public class SearchModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<SearchSugBean> getSearchData(String search) {
        MutableLiveData<SearchSugBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.searchSugUrl + "?key_words=" + search)
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

    public MutableLiveData<GoodsListBean> getBuysData(int pageId) {
        MutableLiveData<GoodsListBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.goodsListUrl + "?page_size=20&page_id=" + pageId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<GoodsListBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(GoodsListBean goodsListBean) {
                        mutableLiveData.postValue(goodsListBean);
                    }
                });

        return mutableLiveData;
    }

}
