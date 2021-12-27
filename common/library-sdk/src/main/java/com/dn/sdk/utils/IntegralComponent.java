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

import java.util.ArrayList;
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
    public void getIntegralList(IntegralHttpCallBack integralHttpCallBack) {
        DoNewsIntegralHolder.getInstance()
                .getIntegralList(new DnIntegralHttpCallBack() {
                    @Override
                    public void onSuccess(List<DnIntegralNativeAd> integralAds) {
//                        //积分拉取成功
//                        IntegralBean integralBean = new IntegralBean(integralAds.get(0));
//                        integralBean.setIntegralList(integralAds);
//                        integralHttpCallBack.onSuccess(integralBean);
                        if (integralAds == null || integralAds.isEmpty()) {
                            integralHttpCallBack.onNoTask();
                        } else {
                            List<ProxyIntegral> resultList = new ArrayList<>();
                            for (int i = 0; i < integralAds.size(); i++) {
                                if (integralAds.get(i).getTaskType().equals("ACTIVATION_TASK")) {
                                    ProxyIntegral integralBean = new ProxyIntegral(integralAds.get(i));
                                    resultList.add(integralBean);
                                }
                            }
                            if (resultList.isEmpty()) {
                                integralHttpCallBack.onNoTask();
                                return;
                            }
                            integralHttpCallBack.onSuccess(resultList);
                        }
                    }

                    @Override
                    public void onError(DnIntegralIntegralError errorInfo) {
                        integralHttpCallBack.onError(errorInfo.getMessage());
                    }
                });
    }


    /**
     * 获取积分单个应用(激活任务)
     */
    public void getIntegral(IntegralHttpCallBack integralHttpCallBack) {
        DoNewsIntegralHolder.getInstance()
                .getIntegralList(new DnIntegralHttpCallBack() {
                    @Override
                    public void onSuccess(List<DnIntegralNativeAd> integralAds) {
                        //积分拉取成功
                        if (integralAds == null || integralAds.isEmpty()) {
                            integralHttpCallBack.onNoTask();
                            return;
                        }
                        for (int i = 0; i < integralAds.size(); i++) {
                            if (integralAds.get(i).getTaskType().equals("ACTIVATION_TASK")) {
                                ProxyIntegral integralBean = new ProxyIntegral(integralAds.get(i));
                                integralHttpCallBack.onSuccess(integralBean);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onError(DnIntegralIntegralError errorInfo) {
                        integralHttpCallBack.onError(errorInfo.getMessage());
                    }
                });
    }


    /**
     * 获取积分单个应用(次留任务,价格最高)
     */
    public void getSecondStayTask(ISecondStayTask iSecondStayTask) {
        DoNewsIntegralHolder.getInstance()
                .getIntegralList(new DnIntegralHttpCallBack() {
                    @Override
                    public void onSuccess(List<DnIntegralNativeAd> integralAds) {
                        //积分拉取成功
                        if (integralAds == null || integralAds.isEmpty()) {
                            iSecondStayTask.onNoTask();
                            return;
                        }
                        ProxyIntegral integralBean = null;
                        for (int i = 0; i < integralAds.size(); i++) {
                            if (integralAds.get(i).getTaskType().equals("RETENTION_TASK")) {
                                if (integralBean == null) {
                                    integralBean = new ProxyIntegral(integralAds.get(i));
                                } else {
                                    if (integralAds.get(i).getPrice() > integralBean.getDnIntegralNativeAd().getPrice()) {
                                        integralBean = new ProxyIntegral(integralAds.get(i));
                                    }
                                }
                            }
                        }
                        if (integralBean != null) {
                            iSecondStayTask.onSecondStayTask(integralBean);
                        }
                    }

                    @Override
                    public void onError(DnIntegralIntegralError errorInfo) {
                        iSecondStayTask.onError(errorInfo.getMessage());
                    }
                });
    }


    public interface IntegralHttpCallBack {
        default void onSuccess(ProxyIntegral var1) {
        }

        //获取一个列表的
        default void onSuccess(List<ProxyIntegral> var1) {
        }

        void onError(String var1);


        void onNoTask();
    }


    public interface ISecondStayTask {
        void onSecondStayTask(ProxyIntegral var1);

        void onError(String var1);

        void onNoTask();
    }


    /**
     * 运行apk
     */
    public void runIntegralApk(Context context, IntegralBean var1) {
        if (var1 != null) {
            var1.getDnIntegralNativeAd().downLoadApk(context, true);
        }
    }

    /**
     * 获取积分列表
     */
    public void setIntegralBindView(Context context, IntegralBean var1, ViewGroup var2, List<View> clickViews, IntegralStateListener integralAdListener, boolean automaticInstallation) {
        if (context == null || var1 == null || var2 == null || clickViews == null) {
            return;
        }
        var1.getDnIntegralNativeAd().bindView(context, var2, clickViews, new DnIntegralAdListener() {

            /**
             * 广告曝光
             * */
            @Override
            public void onAdShow() {
                if (integralAdListener == null) {
                    return;
                }
                integralAdListener.onAdShow();
            }

            /**
             * 广告点击
             *
             * */
            @Override
            public void onAdClick() {
                if (integralAdListener != null) {
                    integralAdListener.onAdClick();
                }
                var1.getDnIntegralNativeAd().downLoadApk(context, automaticInstallation);
            }

            /**
             * 开始下载
             * */

            @Override
            public void onStart() {
                if (integralAdListener == null) {
                    return;
                }
                integralAdListener.onStart();
            }


            /**
             * 下载进度
             * */
            @Override
            public void onProgress(long l, long l1) {
                if (integralAdListener == null) {
                    return;
                }
                integralAdListener.onProgress(l, l1);
            }

            /**
             * 下载完成
             * */

            @Override
            public void onComplete() {
                if (integralAdListener == null) {
                    return;
                }
                integralAdListener.onComplete();
            }

            /**
             * 安装完成
             * */
            @Override
            public void onInstalled() {
                if (integralAdListener == null) {
                    return;
                }
                integralAdListener.onInstalled();
            }

            /**
             *  获取积分墙广告失败
             * */
            @Override
            public void onError(Throwable throwable) {
                if (integralAdListener == null) {
                    return;
                }
                integralAdListener.onError(throwable);
            }
        });
    }


}
