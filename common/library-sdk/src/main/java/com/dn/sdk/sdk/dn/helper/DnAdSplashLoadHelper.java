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
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;
import com.donews.utilslibrary.utils.DensityUtils;

/**
 * 多牛聚合 开屏加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 15:59
 */
public class DnAdSplashLoadHelper {

    private Activity mActivity;
    private RequestInfo mRequestInfo;
    private IAdSplashListener mListener;

    private DoNewsAdNative mDoNewsAdNative;

    public void loadAd(Activity activity, RequestInfo requestInfo, IAdSplashListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;

        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        if (requestInfo.getWidth() == 0) {
            requestInfo.setWidth((int) DensityUtils.px2dp(activity, outMetrics.widthPixels));
        }
        if (requestInfo.getHeight() == 0) {
            requestInfo.setHeight((int) DensityUtils.px2dp(activity, outMetrics.heightPixels));
        }
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
                .setView(mRequestInfo.getContainer())
                .build();

        mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        mDoNewsAdNative.onCreateAdSplash(mActivity, doNewsAD, new DoNewsAdNative.SplashListener() {
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
            public void onShow() {
                if (mListener != null) {
                    mListener.onAdShow();
                }
            }

            @Override
            public void onClicked() {
                if (mListener != null) {
                    mListener.onAdClicked();
                }
            }

            @Override
            public void onPresent() {
                if (mListener != null) {
                    mListener.onPresent();
                }
            }

            @Override
            public void onADDismissed() {
                if (mListener != null) {
                    mListener.onAdDismiss();
                }
            }

            @Override
            public void onNoAD(String s) {
                if (mListener != null) {
                    mListener.onLoadFail(ErrorConstant.ERROR_CODE_NO_AD, s);
                    mListener.onError(ErrorConstant.ERROR_CODE_NO_AD, s);
                }
            }

            @Override
            public void extendExtra(String s) {
                if (mListener != null) {
                    mListener.extendExtra(s);
                }
            }
        });
    }
}
