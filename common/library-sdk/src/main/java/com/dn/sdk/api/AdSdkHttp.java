package com.dn.sdk.api;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.dn.sdk.BuildConfig;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.IntegralAwardBean;
import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by SnowDragon
 * Date on 2020/12/9
 * Description:
 */
public class AdSdkHttp {
    private static final String TAG = "AdSdkHttp";
    /**
     * 是否走本地控制
     */
    protected boolean isLocalControl = false;


    public void getAdConfig() {
        UrlCreator urlCreator = new UrlCreator();
        EasyHttp.get(urlCreator.getAdConfigUrl())
//                .addInterceptor(new HttpLoggingInterceptor("AdSdkHttp").setLevel(HttpLoggingInterceptor.Level.BODY))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<AdConfigBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.e(TAG, "onError " + e.getMessage());
                    }

                    @Override
                    public void onSuccess(AdConfigBean adConfigBean) {
                        SdkLogUtils.i(SdkLogUtils.TAG, " " + adConfigBean.toString());

                        AdConfigSupply.getInstance().setAdConfigBean(adConfigBean);
                    }

                    @Override
                    public void onCompleteOk() {

                    }
                });
    }

    /**
     * 上报数据
     *
     * @param pkName
     * @param appName
     * @param downLoadUrl
     * @param deepLink
     * @param iconUrl
     * @param status
     * @param type
     */
    public void intervalReport(String pkName, String appName, String downLoadUrl,
                               String deepLink, String iconUrl, int status, int type, String txt, String desc) {
        intervalReport(pkName, appName, downLoadUrl, deepLink, iconUrl, status, type, txt, desc, null);

    }

    public void intervalReport(String pkName, String appName, String downLoadUrl,
                               String deepLink, String iconUrl, int status, int type, String txt, String desc, SimpleCallBack<IntegralBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        SdkLogUtils.i(SdkLogUtils.TAG, "pkg: " + pkName + " appName: " + appName
                + " text: " + txt
                + " desc: " + desc
                + " downLoad_Url: " + downLoadUrl);
        try {

            jsonObject.put("pkg", pkName);
            jsonObject.put("name", appName);
            jsonObject.put("download_url", downLoadUrl);
            jsonObject.put("deep_link", deepLink);
            jsonObject.put("icon", iconUrl);
            jsonObject.put("state", status);
            jsonObject.put("type", type);
            jsonObject.put("text", txt);
            jsonObject.put("desc", desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EasyHttp.post(UrlCreator.INTEGRAL_URL)
                .upJson(jsonObject.toString())
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<IntegralBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(IntegralBean integralBean) {
                        if (integralBean != null && integralBean.appList != null && integralBean.appList.size() > 0) {
                            IntegralDataSupply.getInstance().setIntegralBean(integralBean.appList.get(0));
                            if (callBack != null) {
                                callBack.onSuccess(integralBean);
                            }
                        }


                    }
                });


    }

    /**
     * 发放积分奖励
     *
     * @param pkName
     */
    public void giveOutIntervalAward(String pkName, SimpleCallBack<IntegralAwardBean> callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pkg", pkName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EasyHttp.post(UrlCreator.INTEGRAL_AWARD_URL)
                .upJson(jsonObject.toString())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callBack);
    }

    /**
     * 获取积分墙列表
     */
    public void getIntegralList() {
        if (TextUtils.isEmpty(AppInfo.getToken())) {
            return;
        }

        EasyHttp.get(UrlCreator.INTEGRAL_APP_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<IntegralBean>() {
                    @Override
                    public void onError(ApiException e) {
                        IntegralDataSupply.getInstance().setIntegralBean(null);
                    }

                    @Override
                    public void onSuccess(IntegralBean integralBean) {
                        if (integralBean != null && integralBean.appList != null && integralBean.appList.size() > 0) {
                            IntegralDataSupply.getInstance().setIntegralBean(integralBean.appList.get(0));
                        } else {
                            IntegralDataSupply.getInstance().setIntegralBean(null);
                        }

                    }
                });
    }


    public static MutableLiveData<Integer> getCashMoney(double money, String pkg) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pkg", pkg);
            jsonObject.put("money", money);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EasyHttp.post(UrlCreator.INTEGRAL_APP_PAY)
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<Object>() {
                    @Override
                    public void onError(ApiException e) {
                        liveData.postValue(e.getCode());
                    }

                    @Override
                    public void onSuccess(Object obj) {

                        liveData.postValue(0);
                    }

                    @Override
                    public void onCompleteOk() {
                        super.onCompleteOk();
                        liveData.postValue(0);
                    }
                });

        return liveData;
    }

}
