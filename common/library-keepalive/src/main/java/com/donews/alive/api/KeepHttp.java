package com.donews.alive.api;

import com.donews.alive.bean.AppOutBean;
import com.donews.alive.config.DataHelper;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.network.interceptor.HttpLoggingInterceptor;

/**
 * @author by SnowDragon
 * Date on 2020/12/9
 * Description:
 */
public class KeepHttp {
    private static final String TAG = "AdSdkHttp";
    /**
     * 是否走本地控制
     */
    protected boolean isLocalControl = false;

    /**
     * 获取服务端引用外广告配置
     */
    public void getAppOutConfig() {
        if (isLocalControl) {
            return;
        }
        UrlCreator urlCreator = new UrlCreator();

        EasyHttp.get(urlCreator.getUrl())
                .addInterceptor(new HttpLoggingInterceptor("EasyHttp").setLevel(HttpLoggingInterceptor.Level.BODY))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<AppOutBean>() {

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(AppOutBean appOutBean) {
                        updateToLocal(appOutBean);
                    }

                    @Override
                    public void onCompleteOk() {

                    }

                });
    }


    /**
     * 更新服务端数据到本地
     *
     * @param bean bean
     */
    private void updateToLocal(AppOutBean bean) {
        if (bean == null) {
            return;
        }
        DataHelper.getInstance().saveAppDialogBean(bean);
    }

}
