package com.dn.sdk.sdk.tt;

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
import com.dn.sdk.sdk.interfaces.loader.IRealLoader;
import com.dn.sdk.sdk.interfaces.proxy.AdBannerListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdFullVideoListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdInterstitialListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdNativeExpressListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdRewardVideoListenerProxy;
import com.dn.sdk.sdk.interfaces.proxy.AdSplashListenerProxy;
import com.dn.sdk.sdk.tt.helper.TTAdBannerLoadHelper;
import com.dn.sdk.sdk.tt.helper.TTAdFullVideoLoadHelper;
import com.dn.sdk.sdk.tt.helper.TTAdInterstitialLoadHelper;
import com.dn.sdk.sdk.tt.helper.TTAdNativeExpressLoadHelper;
import com.dn.sdk.sdk.tt.helper.TTAdRewardVideoLoadHelper;
import com.dn.sdk.sdk.tt.helper.TTAdSplashLoadHelper;

/**
 * 穿山甲 GroMore 聚合封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 16:00
 */
@SuppressWarnings("unused")
public class PolyTTRealLoader implements IRealLoader {
    @Override
    public SDKType getSdkType() {
        return SDKType.DO_GRO_MORE;
    }

    @Override
    public void loadSplashAd(Activity activity, RequestInfo requestInfo, IAdSplashListener listener) {
        new TTAdSplashLoadHelper().loadSplashAd(activity, requestInfo,
                new AdSplashListenerProxy(requestInfo, listener));
    }

    @Override
    public void loadBannerAd(Activity activity, RequestInfo requestInfo, IAdBannerListener listener) {
        new TTAdBannerLoadHelper().loadBannerAd(activity, requestInfo,
                new AdBannerListenerProxy(requestInfo, listener));
    }

    @Override
    public void loadInterstitialAd(Activity activity, RequestInfo requestInfo, IAdInterstitialListener listener) {
        new TTAdInterstitialLoadHelper().loadAd(activity, requestInfo,
                new AdInterstitialListenerProxy(requestInfo, listener));
    }

    @Override
    public void loadRewardVideoAd(Activity activity, RequestInfo requestInfo, IAdRewardVideoListener listener) {
        new TTAdRewardVideoLoadHelper().loadAd(activity, requestInfo,
                new AdRewardVideoListenerProxy(requestInfo, listener));
    }

    @Override
    public void preloadRewardViewAd(Activity activity, RequestInfo requestInfo,
            IAdPreloadVideoViewListener viewListener, IAdRewardVideoListener listener) {
        new TTAdRewardVideoLoadHelper().preloadAd(activity, requestInfo, viewListener,
                new AdRewardVideoListenerProxy(requestInfo, listener));
    }


    @Override
    public void loadFullVideoAd(Activity activity, RequestInfo requestInfo, IAdFullVideoListener listener) {
        new TTAdFullVideoLoadHelper().loadAd(activity, requestInfo,
                new AdFullVideoListenerProxy(requestInfo, listener));
    }

    @Override
    public void preloadFullVideoAd(Activity activity, RequestInfo requestInfo, IAdPreloadVideoViewListener viewListener,
            IAdFullVideoListener listener) {
        new TTAdFullVideoLoadHelper().preloadAd(activity, requestInfo, viewListener,
                new AdFullVideoListenerProxy(requestInfo, listener));
    }


    @Override
    public void loadFeedNativeAd(Context context, RequestInfo requestInfo, IAdNativeListener listener) {

    }

    @Override
    public void loadFeedNativeExpressAd(Context context, RequestInfo requestInfo, IAdNativeExpressListener listener) {
        new TTAdNativeExpressLoadHelper().loadAd(context, requestInfo,
                new AdNativeExpressListenerProxy(requestInfo, listener));
    }
}
