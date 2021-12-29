package com.module.integral.model;


import androidx.lifecycle.MutableLiveData;

import com.donews.base.BuildConfig;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.integral.bean.IntegralDownloadStateDean;
import com.module.lottery.ui.BaseParams;

import org.json.JSONObject;

import java.util.Map;

//对数据进行访问，并且通知观察者
public class IntegralModel extends BaseLiveDataModel {
    public void getDownloadStatus(MutableLiveData<IntegralDownloadStateDean> mutableLiveData, String url, Map<String, String> params) {
        JSONObject json = new JSONObject(params);

        unDisposable();
        addDisposable(EasyHttp.post(url)
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false).upJson(json.toString())
                .execute(new SimpleCallBack<IntegralDownloadStateDean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(IntegralDownloadStateDean stateDean) {
                        if (stateDean != null) {
                            mutableLiveData.postValue(stateDean);
                        } else {
                            mutableLiveData.postValue(null);
                        }
                    }
                }));
    }


}
