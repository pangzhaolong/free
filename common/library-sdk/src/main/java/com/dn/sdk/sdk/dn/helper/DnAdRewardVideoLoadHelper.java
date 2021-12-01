package com.dn.sdk.sdk.dn.helper;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener;
import com.dn.sdk.sdk.dn.bean.DnNewsPreloadRewardVideoView;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;
import com.orhanobut.logger.Logger;

/**
 * 多牛聚合 激励广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 16:43
 */
public class DnAdRewardVideoLoadHelper {

    private Activity mActivity;
    private RequestInfo mRequestInfo;
    private IAdRewardVideoListener mListener;
    private IAdPreloadVideoViewListener mPreloadVideoViewListener;
    private DoNewsAdNative mDoNewsAdNative;


    public void loadAd(Activity activity, RequestInfo requestInfo, IAdRewardVideoListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;
        load();
    }


    public void preloadAd(Activity activity, RequestInfo requestInfo, IAdPreloadVideoViewListener viewListener,
            IAdRewardVideoListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mPreloadVideoViewListener = viewListener;
        this.mListener = listener;
        preload();
    }

    private void load() {
        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        mDoNewsAdNative.destroy();
                        ((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
                        mActivity = null;
                    }
                }
            });
        }

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(mRequestInfo.getAdId())
                .setOrientation(mRequestInfo.getOrientation())
                .build();
        mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        mDoNewsAdNative.preLoadRewardAd(mActivity, doNewsAD, new DoNewsAdNative.RewardVideoAdCacheListener() {
            @Override
            public void onAdStatus(int i, Object o) {

            }

            @Override
            public void onADLoad() {
                if (mListener != null) {
                    mListener.onLoad();
                }
            }

            @Override
            public void onAdShow() {
                if (mListener != null) {
                    mListener.onRewardAdShow();
                }
            }

            @Override
            public void onAdClose() {
                if (mListener != null) {
                    mListener.onRewardedClosed();
                }
            }

            @Override
            public void onVideoCached() {
                if (mListener != null) {
                    mListener.onLoadCached();
                }
                if (mDoNewsAdNative != null) {
                    mDoNewsAdNative.showRewardAd();
                }
            }

            @Override
            public void onVideoComplete() {
                if (mListener != null) {
                    mListener.onRewardVideoComplete();
                }
            }

            @Override
            public void onAdVideoBarClick() {
                if (mListener != null) {
                    mListener.onRewardBarClick();
                }
            }

            @Override
            public void onRewardVerify(boolean b) {
                if (mListener != null) {
                    mListener.onRewardVerify(b);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (mListener != null) {
                    mListener.onError(i, s);
                }
            }
        });
    }

    private void preload() {
        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        mDoNewsAdNative.destroy();
                        ((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
                        mActivity = null;
                    }
                }
            });
        }

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(mRequestInfo.getAdId())
                .setOrientation(mRequestInfo.getOrientation())
                .build();
        mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();

        DnNewsPreloadRewardVideoView preloadRewardVideoView = new DnNewsPreloadRewardVideoView(mDoNewsAdNative);
        if (mPreloadVideoViewListener != null) {
            mPreloadVideoViewListener.OnLoadVideoView(preloadRewardVideoView);
        }
        mDoNewsAdNative.preLoadRewardAd(mActivity, doNewsAD, new DoNewsAdNative.RewardVideoAdCacheListener() {
            @Override
            public void onAdStatus(int i, Object o) {
                Logger.t("oldAd").d("onAdStatus");
            }

            @Override
            public void onADLoad() {
                Logger.t("oldAd").d("onADLoad");
                if (mListener != null) {
                    mListener.onLoad();
                }
            }

            @Override
            public void onAdShow() {
                Logger.t("oldAd").d("onAdShow");
                if (mListener != null) {
                    mListener.onRewardAdShow();
                }
            }

            @Override
            public void onAdClose() {
                Logger.t("oldAd").d("onAdClose");
                if (mListener != null) {
                    mListener.onRewardedClosed();
                }
            }

            @Override
            public void onVideoCached() {
                Logger.t("oldAd").d("onVideoCached");
                if (mListener != null) {
                    mListener.onLoadCached();
                }
                preloadRewardVideoView.setLoadSuccess(true);
                if (preloadRewardVideoView.isNeedShow()) {
                    preloadRewardVideoView.show();
                }
            }

            @Override
            public void onVideoComplete() {
                Logger.t("oldAd").d("onVideoComplete");
                if (mListener != null) {
                    mListener.onRewardVideoComplete();
                }
            }

            @Override
            public void onAdVideoBarClick() {
                Logger.t("oldAd").d("onAdVideoBarClick");
                if (mListener != null) {
                    mListener.onRewardBarClick();
                }
            }

            @Override
            public void onRewardVerify(boolean b) {
                Logger.t("oldAd").d("onRewardVerify----" + b);
                if (mListener != null) {
                    mListener.onRewardVerify(b);
                }
            }

            @Override
            public void onError(int i, String s) {
                Logger.t("oldAd").d("onRewardVerify----i---" + s);
                if (mListener != null) {
                    mListener.onError(i, s);
                }
            }
        });
    }

}
