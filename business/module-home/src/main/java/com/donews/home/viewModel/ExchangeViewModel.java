package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.api.HomeApi;
import com.donews.home.model.ExchangeModel;
import com.donews.home.model.HomeModel;
import com.donews.middle.bean.home.FactorySaleBean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.TopIconsBean;
import com.donews.middle.bean.home.UserBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

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
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeCategoryBean> getHomeCategoryBean() {
        MutableLiveData<HomeCategoryBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.get(HomeApi.superCategoryUrl)
                .cacheKey("home_category")
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .execute(new SimpleCallBack<HomeCategoryBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeCategoryBean homeBean) {
                        mutableLiveData.postValue(homeBean);
                    }
                });
        return mutableLiveData;
    }

}
