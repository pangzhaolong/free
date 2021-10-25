package com.dn.sdk.sdk.dn;

import android.app.Activity;
import android.content.Context;

import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.bean.SDKType;
import com.dn.sdk.sdk.interfaces.listener.IAdBannerListener;
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener;
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeListener;
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener;
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener;
import com.dn.sdk.sdk.interfaces.loader.ILoader;
import com.dn.sdk.sdk.dn.helper.DnAdBannerLoadHelper;
import com.dn.sdk.sdk.dn.helper.DnAdFullVideoLoadHelper;
import com.dn.sdk.sdk.dn.helper.DnAdInterstitialLoadHelper;
import com.dn.sdk.sdk.dn.helper.DnAdNativeExpressLoadHelper;
import com.dn.sdk.sdk.dn.helper.DnAdRewardVideoLoadHelper;
import com.dn.sdk.sdk.dn.helper.DnAdSplashLoadHelper;
import com.dn.sdk.sdk.interfaces.proxy.AdBannerListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdFullVideoListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdInterstitialListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdRewardVideoListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdSplashListenerProxy;

/**
 * 多牛聚合sdk 加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 15:24
 */
public class DnNewsLoader implements ILoader {
    @Override
    public SDKType getSdkType() {
        return SDKType.DO_NEWS;
    }

    @Override
    public void loadSplashAd(Activity activity, RequestInfo requestInfo, IAdSplashListener listener) {
        new DnAdSplashLoadHelper().loadAd(activity, requestInfo,
                new AdSplashListenerProxy(requestInfo, listener));
    }

    @Override
    public void loadBannerAd(Activity activity, RequestInfo requestInfo, IAdBannerListener listener) {
        new DnAdBannerLoadHelper().loadAd(activity, requestInfo,
                new AdBannerListenerProxy(requestInfo, listener));
    }

    @Override
    public void loadInterstitialAd(Activity activity, RequestInfo requestInfo, IAdInterstitialListener listener) {
        new DnAdInterstitialLoadHelper().loadAd(activity, requestInfo,
                new AdInterstitialListenerProxy(requestInfo, listener));
    }

    @Override
    public void loadRewardVideoAd(Activity activity, RequestInfo requestInfo, IAdRewardVideoListener listener) {
        new DnAdRewardVideoLoadHelper().loadAd(activity, requestInfo,
                new AdRewardVideoListenerProxy(requestInfo, listener));
    }

    @Override
    public void preloadRewardViewAd(Activity activity, RequestInfo requestInfo,
            IAdPreloadVideoViewListener viewListener, IAdRewardVideoListener listener) {
        new DnAdRewardVideoLoadHelper().preloadAd(activity, requestInfo, viewListener,
                new AdRewardVideoListenerProxy(requestInfo, listener));
    }


    @Override
    public void loadFullVideoAd(Activity activity, RequestInfo requestInfo, IAdFullVideoListener listener) {
        new DnAdFullVideoLoadHelper().loadAd(activity, requestInfo,
                new AdFullVideoListenerProxy(requestInfo, listener));
    }

    @Override
    public void preloadFullVideoAd(Activity activity, RequestInfo requestInfo,
            IAdPreloadVideoViewListener viewListener, IAdFullVideoListener listener) {
        new DnAdFullVideoLoadHelper().preloadAd(activity, requestInfo, viewListener,
                new AdFullVideoListenerProxy(requestInfo, listener));
    }


    @Override
    public void loadFeedNativeAd(Context context, RequestInfo requestInfo, IAdNativeListener listener) {

    }

    @Override
    public void loadFeedNativeExpressAd(Context context, RequestInfo requestInfo, IAdNativeExpressListener listener) {
        new DnAdNativeExpressLoadHelper().loadAd(context, requestInfo, listener);
    }
}
