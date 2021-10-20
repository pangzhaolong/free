package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.home.bean.RealTimeBean;
import com.donews.home.bean.SearchSugBean;
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
public class RealTimeModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<RealTimeBean> getRealTimeData(String search) {
        MutableLiveData<RealTimeBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.searchSugUrl + "?key_words=" + search)
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
}
