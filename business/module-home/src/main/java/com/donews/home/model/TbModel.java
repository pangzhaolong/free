package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.home.SearchResultTbBean;
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
public class TbModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<SearchResultTbBean> getSearchResultData(String keyWord) {
        MutableLiveData<SearchResultTbBean> mutableLiveData = new MutableLiveData<>();

        EasyHttp.get(HomeApi.searchResultUrl+"?key_words=" + keyWord)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<SearchResultTbBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(SearchResultTbBean searchResultTbBean) {
                        mutableLiveData.postValue(searchResultTbBean);
                    }
                });

        return mutableLiveData;
    }

}
