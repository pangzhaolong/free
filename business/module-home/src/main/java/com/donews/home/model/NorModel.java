package com.donews.home.model;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.BuildConfig;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.front.LotteryGoodsBean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeExchangeGoodsBean;
import com.donews.middle.bean.home.HomeExchangeReq;
import com.donews.middle.bean.home.HomeExchangeResp;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

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
    @SuppressLint("DefaultLocale")
    public MutableLiveData<HomeGoodsBean> getNorGoodsData(String cids, int pageId) {
        MutableLiveData<HomeGoodsBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(String.format(HomeApi.homeGoodsListUrl, pageId, cids))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeGoodsBean homeGoodsBean) {
                        mutableLiveData.postValue(homeGoodsBean);
                    }
                }));

        return mutableLiveData;
    }

    /**
     * 获取对话的首页列表数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeExchangeGoodsBean> getExchanageGoodsData(
            String category_id, String page_id, int page_size) {
        MutableLiveData<HomeExchangeGoodsBean> mutableLiveData = new MutableLiveData<>();
        String url = BuildConfig.BASE_QBN_API + "exchange/v1/goods-list";
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .params("category_id", category_id)
                .params("page_id", page_id)
                .params("page_size", "" + page_size)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeExchangeGoodsBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeExchangeGoodsBean homeGoodsBean) {
                        mutableLiveData.postValue(homeGoodsBean);
                    }
                });

        return mutableLiveData;
    }

    /**
     * 兑换接口
     */
    public MutableLiveData<HomeExchangeResp> getExchanageData(HomeExchangeReq req) {
        MutableLiveData<HomeExchangeResp> mutableLiveData = new MutableLiveData<>();
        String url = BuildConfig.BASE_QBN_API + "exchange/v1/exchange-goods";
        EasyHttp.post(url)
                .cacheMode(CacheMode.NO_CACHE)
                .upObject(req)
                .execute(new SimpleCallBack<HomeExchangeResp>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeExchangeResp homeGoodsBean) {
                        mutableLiveData.postValue(homeGoodsBean);
                    }
                });

        return mutableLiveData;
    }

}
