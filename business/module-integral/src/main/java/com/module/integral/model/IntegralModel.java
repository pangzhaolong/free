package com.module.integral.model;


import androidx.lifecycle.MutableLiveData;

import com.donews.base.BuildConfig;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.integral.bean.IntegralDownloadStateDean;

import java.util.Map;

//对数据进行访问，并且通知观察者
public class IntegralModel extends BaseLiveDataModel {
    private static String INTEGRAL_BASE = BuildConfig.API_INTEGRAL_URL;
    public static String INTEGRAL_REWARD = INTEGRAL_BASE + "v1/has-wall-reward";
    public void getDownloadStatus(MutableLiveData<IntegralDownloadStateDean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .params(params)
                .execute(new SimpleCallBack<IntegralDownloadStateDean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(IntegralDownloadStateDean stateDean) {
                        if (stateDean != null) {
                            mutableLiveData.postValue(stateDean);
                        }else{
                            mutableLiveData.postValue(null);
                        }
                    }
                }));
    }




}
