/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */

package com.doing.spike.model;

import androidx.lifecycle.MutableLiveData;

import com.doing.spike.bean.SpikeBean;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.orhanobut.logger.Logger;

public class SpikeModel extends BaseLiveDataModel {
    private static String URL = "https://lottery.dev.tagtic.cn/shop/v1/ddq-goods-list";
    private static String PARAMS_TIME = "round_time";

    /**
     * 获取网路数据
     *
     * @return 返回 SpikeBean
     */
    public void getNetData(MutableLiveData<SpikeBean> mutableLiveData, String time) {
        addDisposable(EasyHttp.get(URL)
                .cacheMode(CacheMode.NO_CACHE)
                .params(PARAMS_TIME, time.toString())
                .execute(new SimpleCallBack<SpikeBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(SpikeBean spikeBean) {
                        if(spikeBean!=null){
                            mutableLiveData.postValue(spikeBean);
                        }
                    }
                }));
    }


}
