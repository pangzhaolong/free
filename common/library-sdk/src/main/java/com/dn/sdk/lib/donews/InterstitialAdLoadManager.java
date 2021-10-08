package com.dn.sdk.lib.donews;

import android.app.Activity;

import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.listener.IAdNewsFeedListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.main.info.DoNewsAdNativeData;
import com.donews.b.start.DoNewsAdManagerHolder;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/23
 * Description:
 */
public class InterstitialAdLoadManager {
    public static final String TAG = "InterstitialAdLoadManager";

    public void loadInterstitial(Activity activity, RequestInfo requestInfo, IAdCallBack callBack) {
        SdkLogUtils.i(SdkLogUtils.TAG, " id: " + requestInfo.id);
        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(requestInfo.id)
                //插屏宽度 dp
                .setExpressViewWidth(requestInfo.width)
                //高度 dp
                .setExpressViewHeight(requestInfo.height)
                .build();
        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();

        CountTrackImpl track = new CountTrackImpl(requestInfo);
        doNewsAdNative.onCreateInterstitial(activity, doNewsAD, new DoNewsAdNative.DonewsInterstitialADListener() {

            @Override
            public void onAdError(final String s) {//没有获取到广告
                track.onLoadError();
                SdkLogUtils.i(SdkLogUtils.TAG,"---------------loadInterstitial onAdError "+s);
                if (callBack != null) {
                    callBack.onError(s);
                }
            }

            @Override
            public void showAd() {//显示广告
                SdkLogUtils.i(SdkLogUtils.TAG,"---------------loadInterstitial showAd ");
                track.onShow();
                if (callBack != null) {
                    callBack.onShow();
                }
            }

            @Override
            public void onADExposure() {//广告曝光
                SdkLogUtils.i(SdkLogUtils.TAG,"---------------loadInterstitial onADExposure ");
            }

            @Override
            public void onADClosed() {//广告关闭
                SdkLogUtils.i(SdkLogUtils.TAG,"---------------loadInterstitial onADClosed ");
                //广告关闭 有的渠道是返回的有关闭回调（广点通），有的是自动关闭的（头条，头条点击进去落地页也会自动关闭）。
                track.onAdClose();
                if (callBack != null) {
                    callBack.onClose();
                }
            }

            @Override
            public void onADClicked() {//广告点击
                SdkLogUtils.i(SdkLogUtils.TAG,"---------------loadInterstitial onADExposure ");

                track.onClick();
            }

        });


    }

    public void loadNewsFeed(Activity activity, RequestInfo requestInfo, IAdNewsFeedListener listener) {
        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                //广告位
                .setPositionid(requestInfo.id)
                .setAdCount(requestInfo.adNum)
                .build();
        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        doNewsAdNative.onCreateAdInformation(activity, doNewsAD, new DoNewsAdNative.DoNewsNativesListener() {
            @Override
            public void OnFailed(String s) {//请求广告失败

            }

            @Override
            public void Success(List<DoNewsAdNativeData> list) {//请求信息流广告成功

            }
        });

    }

}
