package com.dn.sdk.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dn.sdk.bean.integral.IntegralBean;
import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.donews.ads.mediation.v2.integral.DnIntegralAdListener;
import com.donews.ads.mediation.v2.integral.DnIntegralNativeAd;
import com.donews.ads.mediation.v2.integral.DoNewsIntegralHolder;
import com.donews.ads.mediation.v2.integral.api.DnIntegralHttpCallBack;
import com.donews.ads.mediation.v2.integral.api.DnIntegralIntegralError;

import java.util.List;

public class IntegralComponent {


    private IntegralComponent() {
    }

    private static volatile IntegralComponent instance = null;

    public static IntegralComponent getInstance() {
        if (instance == null) {
            synchronized (IntegralComponent.class) {
                if (instance == null) {
                    instance = new IntegralComponent();
                }
            }
        }
        return instance;
    }

    /**
     * 获取积分下载列表
     */
    public static void getIntegralList(IntegralHttpCallBack integralHttpCallBack) {
        DoNewsIntegralHolder.getInstance()
                .getIntegralList(new DnIntegralHttpCallBack() {
                    @Override
                    public void onSuccess(List<DnIntegralNativeAd> integralAds) {
//                        //积分拉取成功
//                        IntegralBean integralBean = new IntegralBean(integralAds.get(0));
//                        integralBean.setIntegralList(integralAds);
//                        integralHttpCallBack.onSuccess(integralBean);

                    }

                    @Override
                    public void onError(DnIntegralIntegralError errorInfo) {
                        integralHttpCallBack.onError(errorInfo.getMessage());
                    }
                });
    }


    /**
     * 获取积分单个应用
     */
    public static void getIntegral(IntegralHttpCallBack integralHttpCallBack) {
        DoNewsIntegralHolder.getInstance()
                .getIntegralList(new DnIntegralHttpCallBack() {
                    @Override
                    public void onSuccess(List<DnIntegralNativeAd> integralAds) {
                        //积分拉取成功
                        if (integralAds == null || integralAds.isEmpty()) {
                            integralHttpCallBack.onNoTask();
                            return;
                        }
                        ProxyIntegral integralBean = new ProxyIntegral(integralAds.get(0));
                        integralHttpCallBack.onSuccess(integralBean);

                    }

                    @Override
                    public void onError(DnIntegralIntegralError errorInfo) {
                        integralHttpCallBack.onError(errorInfo.getMessage());
                    }
                });
    }


    public interface IntegralHttpCallBack {
        void onSuccess(ProxyIntegral var1);

        void onError(String var1);

        void onNoTask();
    }


    /**
     * 获取积分列表
     */
    public static void setIntegralBindView(Context context, IntegralBean var1, ViewGroup var2, List<View> clickViews, IntegralStateListener integralAdListener) {
        if (integralAdListener == null || context == null || var1 == null || var2 == null || clickViews == null) {
            return;
        }
        var1.getDnIntegralNativeAd().bindView(context, var2, clickViews, new DnIntegralAdListener() {
            @Override
            public void onAdShow() {
                integralAdListener.onAdShow();
            }

            @Override
            public void onAdClick() {
                integralAdListener.onAdClick();
                var1.getDnIntegralNativeAd().downLoadApk(context, true);
            }

            @Override
            public void onStart() {
                integralAdListener.onStart();
            }

            @Override
            public void onProgress(long l, long l1) {
                integralAdListener.onProgress(l, l1);
            }

            @Override
            public void onComplete() {
                integralAdListener.onComplete();
            }

            @Override
            public void onInstalled() {
                integralAdListener.onInstalled();
            }

            @Override
            public void onError(Throwable throwable) {
                integralAdListener.onError(throwable);
            }
        });
    }


}
