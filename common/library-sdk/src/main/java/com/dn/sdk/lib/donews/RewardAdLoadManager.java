package com.dn.sdk.lib.donews;

import android.app.Activity;
import android.util.Log;

import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdPreLoadVideoListener;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;


/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public class RewardAdLoadManager {
    public static final String TAG = RewardAdLoadManager.class.getSimpleName();

    public void loadRewardVideoAd(Activity activity, RequestInfo requestInfo, AdVideoListener videoListener) {
        requestInfo.adType = AdType.REWARD_VIDEO;

        SdkLogUtils.d(SdkLogUtils.TAG, " doNews  requestInfo id : " + requestInfo.id);

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(requestInfo.id)
                .build();

        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        CountTrackImpl track = new CountTrackImpl(requestInfo);

        doNewsAdNative.onCreateRewardAd(activity, doNewsAD, new RewardVideoAdInnerListener(track, videoListener));


    }

    private class RewardVideoAdInnerListener implements DoNewsAdNative.RewardVideoAdListener {
        CountTrackImpl track;
        AdVideoListener videoListener;

        public RewardVideoAdInnerListener(CountTrackImpl track, AdVideoListener videoListener) {
            this.track = track;
            this.videoListener = videoListener;
        }

        @Override
        public void onAdShow() {
            track.onShow();
            SdkLogUtils.i(SdkLogUtils.TAG, "--------- loadRewardVideoAd:: onAdShow");
            if (videoListener != null) {
                videoListener.onAdShow();
            }

        }

        @Override
        public void onAdVideoBarClick() {
            SdkLogUtils.i(SdkLogUtils.TAG, "--------- loadRewardVideoAd:: onAdVideoBarClick");
            track.onClick();

        }


        @Override
        public void onAdClose() {
            SdkLogUtils.i(SdkLogUtils.TAG, "--------- loadRewardVideoAd:: onAdClose");
            track.onAdClose();

            if (videoListener != null) {
                videoListener.onAdClose();
            }

        }

        @Override
        public void onVideoComplete() {
            SdkLogUtils.i(SdkLogUtils.TAG, "--------- loadRewardVideoAd:: onVideoComplete");
            track.onVideoComplete();

            if (videoListener != null) {
                videoListener.videoComplete(ACache.getInstance().getTopActivity());
            }
        }

        @Override
        public void onRewardVerify(boolean b) {
            SdkLogUtils.i(SdkLogUtils.TAG, " ----------loadRewardVideoAd onRewardVerify : " + b);
            if (videoListener != null) {
                videoListener.onRewardVerify(b);
            }
            track.onRewardVerify(b);
        }

        @Override
        public void onSkippedVideo() {
            SdkLogUtils.i(SdkLogUtils.TAG, " ----------loadRewardVideoAd onSkippedVideo   ");
        }

        @Override
        public void onError(int i, String s) {
            track.onLoadError();
            SdkLogUtils.i(SdkLogUtils.TAG, " ---------loadRewardVideoAd -onError   " + i + "  " + s);
            if (videoListener != null) {
                videoListener.onError(i, s);
            }
        }
    }

    /**
     * 预加载激励视频
     */
    public VideoNative preLoadVideo(Activity mActivity, RequestInfo requestInfo, AdPreLoadVideoListener videoListener) {

        requestInfo.adType = AdType.REWARD_VIDEO;

        //必须是在进入显示激励视频入口的actvity进行预加载，不支持在application或者非激励视频入口的界面进行预加载
        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(requestInfo.id)
                .build();

        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();

        requestInfo.preLoad = true;
        CountTrackImpl track = new CountTrackImpl(requestInfo);

        VideoNative videoNative = new VideoNative(doNewsAdNative, requestInfo.getSdkType());
        //预加载激励视频
        doNewsAdNative.preLoadRewardAd(mActivity, doNewsAD, new RewardVideoAdInnerCacheListener(videoNative, track, videoListener));

        return videoNative;
    }

    private class RewardVideoAdInnerCacheListener implements DoNewsAdNative.RewardVideoAdCacheListener {
        CountTrackImpl track;
        AdPreLoadVideoListener videoListener;
        VideoNative videoNative;

        public RewardVideoAdInnerCacheListener(VideoNative videoNative, CountTrackImpl track, AdPreLoadVideoListener videoListener) {
            this.track = track;
            this.videoListener = videoListener;
            this.videoNative = videoNative;
        }

        @Override
        public void onAdShow() {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------preLoadVideo onAdShow: ");
            track.onShow();

            if (videoListener != null) {
                videoListener.onAdShow();
            }

            if (videoNative.getAdVideoListener() != null) {
                videoNative.getAdVideoListener().onAdShow();
            }
        }

        @Override
        public void onAdVideoBarClick() {
            track.onClick();

        }

        @Override
        public void onAdClose() {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------preLoadVideo onAdClose: ");
            track.onAdClose();

            if (videoListener != null) {
                videoListener.onAdClose();
            }

            if (videoNative.getAdVideoListener() != null) {
                videoNative.getAdVideoListener().onAdClose();
            }
        }

        @Override
        public void onVideoComplete() {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------preLoadVideo onVideoComplete: ");
            track.onVideoComplete();
            if (videoListener != null) {
                videoListener.videoComplete(ACache.getInstance().getTopActivity());
            }
        }

        @Override
        public void onRewardVerify(boolean b) {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------preLoadVideo  onRewardVerify: " + b);
            if (videoListener != null) {
                videoListener.onRewardVerify(b);
            }
            track.onRewardVerify(b);

        }

        @Override
        public void onError(int i, String s) {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------preLoadVideo onErrorCode: " + i + "  mes: " + s);
            track.onLoadError();
            if (videoListener != null) {
                videoListener.onError(i, s);
            }
            if (videoNative.getAdVideoListener() != null) {
                videoNative.getAdVideoListener().onError(i, s);
            }
        }

        @Override
        public void onADLoad() {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------ preLoadVideo onADLoad: ");
        }

        /**
         * 缓存成功
         */
        @Override
        public void onVideoCached() {
            SdkLogUtils.i(SdkLogUtils.TAG, "------------preLoadVideo  onVideoCached: ");
            videoNative.setReady(true);
            if (videoListener != null) {
                videoListener.loadSuccess(videoNative);
            }
        }
    }

    /**
     * 预加载全屏广告
     *
     * @param mActivity     activity
     * @param requestInfo   广告信息
     * @param videoListener 监听
     * @return
     */
    public DoNewsAdNative preLoadFullScreenVideo(Activity mActivity, RequestInfo requestInfo, AdVideoListener videoListener) {
        return fullScreenVideo(mActivity, true, requestInfo, videoListener);
    }

    /**
     * 载全屏广告
     *
     * @param mActivity     activity
     * @param requestInfo   广告信息
     * @param videoListener 监听
     * @return
     */
    public DoNewsAdNative loadFullScreenVideo(Activity mActivity, RequestInfo requestInfo, AdVideoListener videoListener) {
        return fullScreenVideo(mActivity, false, requestInfo, videoListener);
    }


    /**
     * 预加载全屏视频
     *
     * @param mActivity     预加载全屏视频
     * @param requestInfo   加载信息
     * @param videoListener 监听
     * @return
     */
    private DoNewsAdNative fullScreenVideo(Activity mActivity, boolean isPreLoad, RequestInfo requestInfo, AdVideoListener videoListener) {

        requestInfo.adType = AdType.FULL_SCREEN_VIDEO;

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                //广告位ID
                .setPositionid(requestInfo.id)
                //1竖屏2横屏
                .setOrientation(1)
                .setExpressViewHeight(requestInfo.height)
                .setExpressViewWidth(requestInfo.width)
                .build();
        CountTrackImpl track = new CountTrackImpl(requestInfo);

        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();

        doNewsAdNative.preLoadFullScreenVideoAd(mActivity, doNewsAD, new DoNewsAdNative.FullSreenVideoListener() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                track.onLoadError();

                if (videoListener != null) {
                    videoListener.onError(errorCode, errorMsg);
                }

            }

            @Override
            public void onVideoLoad() {
                Log.i(TAG, "loadFullScreenVideo onVideoLoad");
            }

            @Override
            public void onVideoCached() {
                Log.i(TAG, "loadFullScreenVideo onVideoCached");
                if (!isPreLoad) {
                    doNewsAdNative.showFullScreenVideo();
                }
            }

            @Override
            public void onAdShow() {
                track.onShow();

                if (videoListener != null) {
                    videoListener.onAdShow();
                }

            }

            @Override
            public void onAdClick() {
                track.onClick();

            }

            @Override
            public void onAdClose() {
                track.onAdClose();

                if (videoListener != null) {
                    videoListener.onAdClose();
                }
            }

            @Override
            public void onVideoComplete() {

            }

            @Override
            public void onSkippedVideo() {

            }
        });

        return doNewsAdNative;
    }


}
