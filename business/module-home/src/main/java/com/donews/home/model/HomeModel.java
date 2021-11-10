package com.donews.home.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.home.api.HomeApi;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.UserBean;
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
public class HomeModel extends BaseLiveDataModel {


    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeCategoryBean> getNetData() {
        MutableLiveData<HomeCategoryBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(HomeApi.SuperCategory)
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
                }));

        return mutableLiveData;
    }

    public MutableLiveData<SecKilBean> getSecKilData() {
        MutableLiveData<SecKilBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(HomeApi.seckiltUrl + "?&page_size=4")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<SecKilBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(SecKilBean secKilBean) {
                        mutableLiveData.postValue(secKilBean);
                    }
                }));

        return mutableLiveData;
    }

    public MutableLiveData<UserBean> getUserList() {
        MutableLiveData<UserBean> mutableLiveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(HomeApi.userRandomUrl + "?limit=3")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(UserBean userBean) {
                        mutableLiveData.postValue(userBean);
                    }
                }));

        return mutableLiveData;
    }
}
