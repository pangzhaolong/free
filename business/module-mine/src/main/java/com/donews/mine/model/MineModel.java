package com.donews.mine.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.mine.bean.QueryBean;
import com.donews.mine.Api.MineHttpApi;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;

import io.reactivex.disposables.Disposable;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 15:30<br>
 * 版本：V1.0<br>
 */
public class MineModel extends BaseLiveDataModel {

    private Disposable disposable;


    /**
     * 获取金币明细
     */
    public MutableLiveData<QueryBean> getQuery() {
        MutableLiveData<QueryBean> liveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(MineHttpApi.QUERY)
                .params("score_type", "balance")
                .params("user_id", AppInfo.getUserId())
                .params("app_name", DeviceUtils.getPackage())
                .params("suuid", DeviceUtils.getMyUUID())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<QueryBean>() {
                    @Override
                    public void onError(ApiException e) {
                        liveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(QueryBean queryBean) {

                        liveData.postValue(queryBean);
                    }
                }));
        return liveData;
    }


}
