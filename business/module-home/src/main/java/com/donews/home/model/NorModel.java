package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.home.bean.HomeBean;
import com.donews.home.bean.NorGoodsBean;
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
public class NorModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<NorGoodsBean> getNorGoodsData(String cids) {
        MutableLiveData<NorGoodsBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(HomeApi.goodsList + "?cids=" + cids)
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
