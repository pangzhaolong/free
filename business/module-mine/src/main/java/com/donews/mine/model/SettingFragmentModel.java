package com.donews.mine.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.dn.drouter.ARouteHelper;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.base.model.BaseModel;
import com.donews.common.download.DownloadListener;
import com.donews.common.download.DownloadManager;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.bean.CacheBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppCacheUtils;
import com.donews.utilslibrary.utils.DeviceUtils;

import io.reactivex.disposables.Disposable;

public class SettingFragmentModel extends BaseLiveDataModel {

    private final CacheBean cacheBean = new CacheBean();
    private Disposable disposable;
    private boolean isremind = false;





    public void clearCache(Context context) {
        AppCacheUtils.clearAllCache(context);
        cacheBean.setCacheValue(cacheData(context));
    }

    public void getCacheData(Context context) {
        cacheBean.setCacheValue(cacheData(context));
        cacheBean.setVersionName(DeviceUtils.getVersionName());

    }

    public String cacheData(Context context) {
        try {
            String cacheSize = AppCacheUtils.getTotalCacheSize(context);
            return cacheSize;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }


}
