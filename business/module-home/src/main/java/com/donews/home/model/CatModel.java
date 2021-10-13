package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.bean.HomeBean;
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
public class CatModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeBean> getNetData() {
        MutableLiveData<HomeBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.post("")
                .upJson("")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeBean homeBean) {
                        mutableLiveData.postValue(homeBean);
                    }
                }));

        return mutableLiveData;
    }

}
