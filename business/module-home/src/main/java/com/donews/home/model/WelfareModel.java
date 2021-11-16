package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.home.PerfectGoodsBean;
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
public class WelfareModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<PerfectGoodsBean> getPerfectGoodsData(String from, int pageId) {
        int src = 1;
        if (from.equalsIgnoreCase("tb")) {
            src = 1;
        } else if (from.equalsIgnoreCase("pdd")) {
            src = 2;
        } else if (from.equalsIgnoreCase("jd")) {
            src = 3;
        }

        MutableLiveData<PerfectGoodsBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.perfectGoodsListUrl + "?page_size=20&page_id=" + pageId + "&src=" + src)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<PerfectGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(PerfectGoodsBean perfectGoodsBean) {
                        mutableLiveData.postValue(perfectGoodsBean);
                    }
                });

        return mutableLiveData;
    }
}
