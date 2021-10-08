package com.dn.sdk.lib.donews;

import android.app.Activity;
import android.view.View;

import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.count.ITrack;
import com.dn.sdk.lib.IAdController;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdPreLoadVideoListener;
import com.dn.sdk.listener.AdSplashListener;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.listener.IAdNewsFeedListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;

/**
 * @author by SnowDragon
 * Date on 2021/1/15
 * Description:
 */
public class DoNewsController implements IAdController {


    @Override
    public void loadAdSplash(Activity activity, RequestInfo requestInfo, AdSplashListener splashListener) {
        new SplashAdLoadManager().loadAd(activity, requestInfo, splashListener);
    }


    @Override
    public void loadVideo(Activity activity, RequestInfo requestInfo, AdVideoListener videoListener) {
        new RewardAdLoadManager().loadRewardVideoAd(activity, requestInfo, videoListener);
    }

    @Override
    public VideoNative preLoadVideo(Activity activity, RequestInfo requestInfo, AdPreLoadVideoListener videoListener) {
        RewardAdLoadManager loadManager = new RewardAdLoadManager();
        return loadManager.preLoadVideo(activity, requestInfo, videoListener);
    }

    public DoNewsAdNative fullScreenVideo(Activity activity, boolean isPreLoad, RequestInfo requestInfo, AdVideoListener videoListener) {
        RewardAdLoadManager loadManager = new RewardAdLoadManager();
        return loadManager.loadFullScreenVideo(activity, requestInfo, videoListener);
    }

    @Override
    public void loadNewsFeedTemplate(Activity activity, RequestInfo requestInfo,boolean cacheAd, IAdCallBack iAdCallBack) {
        new NewsFeedAdLoadManager().loadNewsFeedTemplate(activity, requestInfo, cacheAd, iAdCallBack);
    }

    @Override
    public void loadNewsFeedCustomRender(Activity activity, RequestInfo requestInfo, int layoutId, IAdNewsFeedListener listener) {
        new NewsFeedAdLoadManager().loadNewsFeedCustomRender(activity, requestInfo, layoutId, listener);
    }

    @Override
    public void loadInterstitial(Activity activity, RequestInfo requestInfo, IAdCallBack iAdCallBack) {
        new InterstitialAdLoadManager().loadInterstitial(activity, requestInfo, iAdCallBack);
    }

    @Override
    public void loadBanner(Activity activity, RequestInfo requestInfo, IAdCallBack iAdCallBack) {

        ITrack iTrack = new CountTrackImpl(requestInfo);
        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(requestInfo.id)
                //申请广告的宽  必传参数 dp
                .setExpressViewWidth(requestInfo.width)
                //申请广澳的高 必传参数，dp 0是自适应，强烈建议为0，自适应高度
                .setExpressViewHeight(0)
                //容器可以传入，传入的话SDK会自动把广告add进去，如果不传，则需要app自己add进去
                .setView(requestInfo.container)
                .build();
        SdkLogUtils.i(SdkLogUtils.TAG, " id: " + requestInfo.id);
        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        doNewsAdNative.onCreateBanner(activity, doNewsAD, new DoNewsAdNative.DoNewsBannerADListener() {
            @Override
            public void onAdError(final String s) {
                SdkLogUtils.i(SdkLogUtils.TAG, " :" + s);
                iTrack.onLoadError();
                if (iAdCallBack != null) {
                    iAdCallBack.onError(s);
                }

            }

            @Override
            public void showAd() {//显示广告
                iTrack.onShow();
                if (iAdCallBack != null) {
                    iAdCallBack.onShow();
                }
            }

            @Override
            public void onRenderSuccess(View view) {//如果传入rl_banner_container，请不要在此自己addView，如果没有传入，则需要自己添加
            }

            @Override
            public void onADExposure() {//广告曝光
            }

            @Override
            public void onADClosed() {//百度没有关闭回调
            }

            @Override
            public void onADClicked() {//广告点击
                iTrack.onClick();

            }
        });
    }
}
