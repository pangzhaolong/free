package com.dn.sdk.sdk.dn.helper;


import android.app.Activity;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.dn.sdk.sdk.ErrorConstant;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;

/**
 * 多牛聚合 插屏广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 16:39
 */
public class DnAdInterstitialLoadHelper {

    private Activity mActivity;
    private RequestInfo mRequestInfo;
    private IAdInterstitialListener mListener;

    private DoNewsAdNative mDoNewsAdNative;


    public void loadAd(Activity activity, RequestInfo requestInfo, IAdInterstitialListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;

        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        if (requestInfo.getWidth() == 0) {
            requestInfo.setWidth(outMetrics.widthPixels);
        }

        //多牛sdk,高度设置为0，内容自适应
        requestInfo.setHeight(0);

        load();
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
                .setExpressViewWidth(mRequestInfo.getWidth())
                .setExpressViewHeight(mRequestInfo.getHeight())
                .build();

        mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        mDoNewsAdNative.onCreateInterstitial(mActivity, doNewsAD, new DoNewsAdNative.DonewsInterstitialADListener() {
            @Override
            public void onAdStatus(int i, Object o) {

            }

            @Override
            public void onAdLoad() {
                if (mListener != null) {
                    mListener.onLoad();
                }
            }

            @Override
            public void onAdShow() {
                if (mListener != null) {
                    mListener.onAdShow();
                }
            }

            @Override
            public void onADClosed() {
                if (mListener != null) {
                    mListener.onAdClosed();
                }
            }

            @Override
            public void onADClicked() {
                if (mListener != null) {
                    mListener.onAdClicked();
                }
            }

            @Override
            public void onADExposure() {
                if (mListener != null) {
                    mListener.onAdExposure();
                }
            }

            @Override
            public void onAdError(String s) {
                if (mListener != null) {
                    mListener.onAdShowFail(ErrorConstant.ERROR_CODE_NO_AD, s);
                    mListener.onError(ErrorConstant.ERROR_CODE_NO_AD, s);
                }
            }
        });
    }

}
