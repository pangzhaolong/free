package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.api.HomeApi;
import com.donews.home.model.ExchangeModel;
import com.donews.home.model.HomeModel;
import com.donews.middle.BuildConfig;
import com.donews.middle.bean.home.FactorySaleBean;
import com.donews.middle.bean.home.HomeCategory2Bean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeCoinCritConfigBean;
import com.donews.middle.bean.home.HomeEarnCoinReq;
import com.donews.middle.bean.home.HomeEarnCoinResp;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.TopIconsBean;
import com.donews.middle.bean.home.UserBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class ExchangeViewModel extends BaseLiveDataViewModel<ExchangeModel> {

    @Override
    public ExchangeModel createModel() {
        return new ExchangeModel();
    }

    /**
     * 赚金币的接口
     *
     * @return
     */
    public MutableLiveData<HomeEarnCoinResp> getEarnCoin(HomeEarnCoinReq req) {
        MutableLiveData<HomeEarnCoinResp> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/earn-coin");
        EasyHttp.post(url)
                .upObject(req)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeEarnCoinResp>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeEarnCoinResp homeBean) {
                        mutableLiveData.postValue(homeBean);
                        //如果成功了。同步更新一次配置信息
                        getCoinCritConfig();
                    }
                });
        return mutableLiveData;
    }

    /**
     * 获取赚金币暴击模式配置信息
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeCoinCritConfigBean> getCoinCritConfig() {
        MutableLiveData<HomeCoinCritConfigBean> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/coin-crit-config");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .cacheKey("home_coin_crit_config")
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .execute(new SimpleCallBack<HomeCoinCritConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        HomeCoinCritConfigBean item = querySaveCoinCritConfig();
                        if (item != null) {
                            mutableLiveData.postValue(item);
                            return;
                        }
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeCoinCritConfigBean homeBean) {
                        saveCoinCritConfig(homeBean);
                        mutableLiveData.postValue(homeBean);
                    }
                });
        return mutableLiveData;
    }

    /**
     * 查询保存本地的配置
     *
     * @return
     */
    public HomeCoinCritConfigBean querySaveCoinCritConfig() {
        try {
            String saveFile = "coinCritConfigFile";
            String saveKey = "coinCritConfigKey";
            String json = SPUtils.getInstance(saveFile)
                    .getString(saveKey);
            HomeCoinCritConfigBean item = GsonUtils.fromJson(json, HomeCoinCritConfigBean.class);
            return item;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeCategory2Bean> getHomeCategoryBean() {
        MutableLiveData<HomeCategory2Bean> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/categories");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .cacheKey("home_category2")
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .execute(new SimpleCallBack<HomeCategory2Bean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeCategory2Bean homeBean) {
                        mutableLiveData.postValue(homeBean);
                    }
                });
        return mutableLiveData;
    }

    /**
     * 保存配置信息
     *
     * @param bean
     */
    private void saveCoinCritConfig(HomeCoinCritConfigBean bean) {
        try {
            String saveFile = "coinCritConfigFile";
            String saveKey = "coinCritConfigKey";
            SPUtils.getInstance(saveFile)
                    .put(saveKey, GsonUtils.toJson(bean));
        } catch (Exception e) {
            if (BuildConfig.HTTP_DEBUG) {
                throw e;
            }
        }
    }
}
