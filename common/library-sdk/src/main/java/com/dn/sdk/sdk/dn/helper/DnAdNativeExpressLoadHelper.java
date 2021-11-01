package com.dn.sdk.sdk.dn.helper;


import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.dn.sdk.sdk.ErrorConstant;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;
import com.donews.utilslibrary.utils.DensityUtils;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * 多牛聚合 信息流模板加载辅助类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 17:11
 */
public class DnAdNativeExpressLoadHelper {


    private Context mContext;
    private RequestInfo mRequestInfo;
    private IAdNativeExpressListener mListener;

    private DoNewsAdNative mDoNewsAdNative;


    public void loadAd(Context context, RequestInfo requestInfo, IAdNativeExpressListener listener) {
        this.mContext = context;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;
        load();
    }

    private void load() {
        if (mContext instanceof AppCompatActivity) {
            ((AppCompatActivity) mContext).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        if (mDoNewsAdNative != null) {
                            mDoNewsAdNative.destroy();
                        }
                        ((AppCompatActivity) mContext).getLifecycle().removeObserver(this);
                        mContext = null;
                    }
                }
            });
        }

        if (mRequestInfo.getWidth() == 0) {
            int widthDp = (int) DensityUtils.px2dp(DeviceUtils.getWidthPixels((Activity) mContext));
            mRequestInfo.setWidth(widthDp);
        }
        mRequestInfo.setHeight(0);
        if (mRequestInfo.getNativeAdCount() == 0) {
            mRequestInfo.setNativeAdCount(1);
        }

        Logger.d(mRequestInfo);
        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(mRequestInfo.getAdId())
                .setExpressViewWidth(mRequestInfo.getWidth())
                .setExpressViewHeight(mRequestInfo.getHeight())
                .setAdCount(mRequestInfo.getNativeAdCount())
                .build();


        mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        mDoNewsAdNative.onCreatTemplateAd((Activity) mContext, doNewsAD, new DoNewsAdNative.DoNewsTemplateListener() {
            @Override
            public void onAdClose() {
                if (mListener != null) {
                    mListener.onAdClose();
                }
            }

            @Override
            public void onADClicked() {
                if (mListener != null) {
                    mListener.onAdClick();
                }
            }

            @Override
            public void onADExposure() {


            }

            @Override
            public void onNoAD(String s) {
                if (mListener != null) {
                    mListener.onError(ErrorConstant.ERROR_CODE_NO_AD, s);
                }
            }

            @Override
            public void onAdError(String s) {
                Logger.d("多牛模板广告加载失败:%s ", s);
                if (mListener != null) {
                    mListener.onError(ErrorConstant.ERROR_CODE_UNKNOWN, s);
                }
            }

            @Override
            public void onADLoaded(List<View> list) {
                Logger.d("多牛模板广告加载成功:size = " + list.size());
                if (mListener != null) {
                    mListener.onLoad(list);
                }
            }
        });
//		mDoNewsAdNative.onCreatTemplateAd((Activity) mContext, doNewsAD, new DoNewsAdNative
//		.DoNewsExpressTemplateListener() {
//			@Override
//			public void onAdError(String s) {
//				Logger.d("多牛模板广告加载失败:%s ", s);
//				if (mListener != null) {
//					mListener.onLoadFail(ERROR_CODE_NO_AD, s);
//					mListener.onError(ERROR_CODE_NO_AD, s);
//				}
//			}
//
//			@Override
//			public void onADLoaded(List<DoNewsNativeExpressAd> list) {
//				Logger.d("多牛模板广告加载成功:size = " + list.size());
//				List<View> views = new ArrayList<>();
//				for (DoNewsNativeExpressAd ad : list) {
//					View view = ad.getExpressAdView();
//					views.add(view);
//					ad.setExpressInteractionListener(new DoNewsNativeExpressAd.ExpressAdInteractionListener() {
//						@Override
//						public void onAdShow() {
//							if (mListener != null) {
//								mListener.onAdShow();
//							}
//						}
//
//						@Override
//						public void onAdClicked() {
//							if (mListener != null) {
//								mListener.onAdClick();
//							}
//						}
//
//						@Override
//						public void onAdClose() {
//							if (mListener != null) {
//								mListener.onCancel();
//							}
//						}
//
//						@Override
//						public void onRenderFail(String s, int i) {
//							if (mListener != null) {
//								mListener.onRenderFail(view, s, i);
//							}
//						}
//
//						@Override
//						public void onRenderSuccess() {
//							if (mListener != null) {
//								mListener.onRenderSuccess(view.getWidth(), view.getHeight());
//							}
//						}
//					});
//					ad.setVideoAdListener(new DoNewsNativeExpressAd.ExpressVideoAdListener() {
//						@Override
//						public void onVideoLoad() {
//							if (mListener != null) {
//
//							}
//						}
//
//						@Override
//						public void onVideoAdStartPlay() {
//							if (mListener != null) {
//								mListener.onVideoStart();
//							}
//						}
//
//						@Override
//						public void onVideoAdContinuePlay() {
//
//						}
//
//						@Override
//						public void onClickRetry() {
//
//						}
//
//						@Override
//						public void onVideoAdPaused() {
//							if (mListener != null) {
//								mListener.onVideoPause();
//							}
//						}
//
//						@Override
//						public void onVideoAdComplete() {
//							if (mListener != null) {
//								mListener.onVideoCompleted();
//							}
//						}
//
//						@Override
//						public void onVideoError(int i, String s) {
//							if (mListener != null) {
//								mListener.onVideoError(i, s);
//							}
//						}
//					});
//					ad.render();
//				}
//
//				if (mListener != null) {
//					mListener.onLoad(views);
//				}
//			}
//		});

    }
}
