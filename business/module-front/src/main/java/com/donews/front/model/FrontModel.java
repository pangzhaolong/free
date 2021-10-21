package com.donews.front.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.front.api.FrontApi;
import com.donews.front.bean.FrontBean;
import com.donews.front.bean.LotteryCategoryBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

public class FrontModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 frontBean的数据
     */
    public MutableLiveData<LotteryCategoryBean> getNetData() {
        MutableLiveData<LotteryCategoryBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.lotteryCategoryUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<LotteryCategoryBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(LotteryCategoryBean categoryBean) {
                        mutableLiveData.postValue(categoryBean);
                    }
                }));

        return mutableLiveData;
    }
}
