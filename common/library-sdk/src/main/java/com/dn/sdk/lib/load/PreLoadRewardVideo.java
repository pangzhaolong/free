package com.dn.sdk.lib.load;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.dn.sdk.AdLoadManager;
import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SdkManager;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdPreLoadVideoListener;
import com.dn.sdk.utils.SdkLogUtils;

import java.util.LinkedList;

/**
 * @author by SnowDragon
 * Date on 2021/1/19
 * Description:
 */
public class PreLoadRewardVideo {

    private final FragmentActivity activity;
    private final RequestInfo requestInfo;
    private final AdPreLoadVideoListener videoListener;

    LinkedList<AdConfigBean.AdID> adIdS;
    private VideoNative videoNative;

    public PreLoadRewardVideo(FragmentActivity activity, VideoNative videoNative,
                              RequestInfo requestInfo, AdPreLoadVideoListener videoListener) {

        this.activity = activity;
        this.requestInfo = requestInfo;
        this.videoListener = videoListener;
        requestInfo.adType = AdType.REWARD_VIDEO;

        this.videoNative = videoNative;
    }

    public void loadAd() {
        SdkLogUtils.i(SdkLogUtils.TAG, "");
        if (ACache.getInstance().hasVideoNativeCache()) {
            return;
        }
        adIdS = AdConfigSupply.getInstance().getAdIdList(requestInfo.adType);

        SdkLogUtils.i(SdkLogUtils.TAG, " -- preloadRewardVideo: size：  " + adIdS);
        preLoadVideo();

    }

    private void preLoadVideo() {
        //循环加载结束
        if (adIdS.isEmpty()) {
            if (videoListener != null) {
                videoListener.onError(0, "");
            }
            return;
        }


        AdConfigBean.AdID adID = adIdS.poll();

        AdConfigSupply.getInstance().wrapperRequestInfo(adID, requestInfo);

        SdkManager.getInstance().getAdController(requestInfo.getSdkType())
                .preLoadVideo(activity, requestInfo, new AdPreLoadVideoListener() {

                    @Override
                    public void loadSuccess(VideoNative ad) {
                        SdkLogUtils.i(SdkLogUtils.TAG, " -------------------loadSuccess VideoNative- " + ad);
                        videoNative.setReady(true);
                        ad.wrapper(videoNative);
                        ACache.getInstance().cacheVideoNative(videoNative);

//                        if (requestInfo.getSdkType() == SDKType.DO_NEWS) {
                            ACache.getInstance().registerRewardVideoListener(this);
//                        }
                    }

                    @Override
                    public void onAdShow() {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》onAdShow ");
                        if (videoNative.getAdVideoListener() != null) {
                            videoNative.getAdVideoListener().onAdShow();
                        }

                        if (videoListener != null) {
                            videoListener.onAdShow();
                        }

                    }

                    @Override
                    public void onAdClose() {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》onAdClose ");
                        if (videoListener != null) {
                            videoListener.onAdClose();
                        }
                        if (videoNative.getAdVideoListener() != null) {
                            videoNative.getAdVideoListener().onAdClose();
                        }
                        AdConfigSupply.getInstance().setVideoUpPlayTime(System.currentTimeMillis());

                        //预加载激励视频关闭的时候，缓存下一个
                        if (activity != null && AdConfigSupply.getInstance().isShowInterstitial()) {
                            AdLoadManager.getInstance()
                                    .cacheRewardVideo(activity, new RequestInfo(AdIdConfig.REWARD_VIDEO_ID), null);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》onError ");
                        if (videoNative.getAdVideoListener() != null) {
                            videoNative.getAdVideoListener().onError(errorCode, errorMsg);
                        }
                        requestInfo.usePassId = false;
                        preLoadVideo();
                    }

                    @Override
                    public void videoComplete(Activity activity) {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》complete ");
                        if (videoListener != null) {
                            videoListener.videoComplete(activity);
                        }
                        //播放完成加载插屏广告
                        if (activity != null && AdConfigSupply.getInstance().isShowInterstitial()) {
                            AdLoadManager.getInstance()
                                    .loadInterstitial(activity, new RequestInfo(AdIdConfig.INTERSTITIAL_ID));
                        }

                    }

                    @Override
                    public void onRewardVerify(boolean rewardVerify) {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》onRewardVerify " + rewardVerify);
                    }

                    @Override
                    public void videoCoolDownIng() {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》videoCoolDownIng ");
                    }
                });

    }
}
