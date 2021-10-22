package com.donews.front.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.front.api.FrontApi;
import com.donews.front.bean.FrontBean;
import com.donews.front.bean.NorGoodsBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

public class NorModel extends BaseLiveDataModel {

    /**
     * 获取网路数据
     *
     * @return 返回 frontBean的数据
     */
    public MutableLiveData<NorGoodsBean> getNetData(String categoryId, String pageId) {
        MutableLiveData<NorGoodsBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(FrontApi.lotteryGoodsUrl +"?category_id="+categoryId+"&page_size=20")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<NorGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(NorGoodsBean norGoodsBean) {
                        mutableLiveData.postValue(norGoodsBean);
                    }
                }));

        return mutableLiveData;
    }
}
