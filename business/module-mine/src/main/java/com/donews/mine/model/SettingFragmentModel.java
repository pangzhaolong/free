package com.donews.mine.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.dn.drouter.ARouteHelper;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.base.model.BaseModel;
import com.donews.common.contract.ApplyUpdataBean;
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




    //----------------------检查更新-------------------

    public  MutableLiveData<ApplyUpdataBean> applyUpdate( ) {
        MutableLiveData<ApplyUpdataBean> liveData = new MutableLiveData<>();
        addDisposable( EasyHttp.get(MineHttpApi.APK_INFO)
                .params("package_name", DeviceUtils.getPackage())
                .params("channel", DeviceUtils.getChannelName())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ApplyUpdataBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(ApplyUpdataBean updataBean) {
                        liveData.postValue(updataBean);

                    }

                }));
        return liveData;

    }

    public static void downLoadApk(Context context,
                                   ApplyUpdataBean applyUpdataBean,
                                   BaseModel model) {
        DownloadManager downloadManager = new DownloadManager(context,context.getPackageName(), applyUpdataBean.getApk_url(), new DownloadListener() {
            @Override
            public void updateProgress(int progress) {
                applyUpdataBean.setProgress(progress);
            }

            @Override
            public void downloadComplete(String pkName,String path) {
                applyUpdataBean.setProgress(100);
                if (model != null) {
                    model.loadComplete();
                    return;
                }
                ARouteHelper.invoke(RouterFragmentPath.ClassPath.HOME_VIEW_MODEL, "downLoadEnd", "下载完成");
            }

            @Override
            public void downloadError(String error) {
                if (model != null) {
                    model.loadFail("下载失败");
                    return;
                }
                ARouteHelper.invoke(RouterFragmentPath.ClassPath.HOME_VIEW_MODEL, "downLoadEnd", "下载失败");
            }
        });
        downloadManager.setImmInstall(true);
        downloadManager.start();
    }


}
