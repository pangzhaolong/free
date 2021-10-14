package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.home.bean.DataBean;
import com.donews.home.bean.SearchSugBean;
import com.donews.home.bean.TopGoodsBean;
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
//        https://lottery.dev.tagtic.cn/shop/v1/search-suggestion?key_words=%E5%93%88%E5%93%88&type=1
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
}
