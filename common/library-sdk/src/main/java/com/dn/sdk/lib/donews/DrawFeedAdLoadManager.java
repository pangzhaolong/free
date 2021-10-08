package com.dn.sdk.lib.donews;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.listener.IAdDrawFeedListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.main.info.DoNewsExpressDrawFeedAd;
import com.donews.b.start.DoNewsAdManagerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public class DrawFeedAdLoadManager {
    public static final String TAG = "DrawFeedAdLoadManager";

    public static int countRenderNum = 0;

    private void loadDrawNativeAd(Activity activity, RequestInfo requestInfo, IAdDrawFeedListener listener) {
        String adPosition = AdConfigSupply.getInstance().getDrawFeedId();

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                //广告位id
                .setPositionid(TextUtils.isEmpty(adPosition) ? requestInfo.id : adPosition)
                .setExpressViewWidth(requestInfo.width)
                .setExpressViewHeight(requestInfo.height)
                //1到3
                .setAdCount(requestInfo.adNum)
                .build();

        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        doNewsAdNative.loadDrawFeedAd(activity, doNewsAD, new DoNewsAdNative.DrawFeedAdListener() {
            @Override
            public void onError(int code, String errMsg) {//获取数据失败
                SdkLogUtils.i(TAG, errMsg);
            }

            @Override
            public void onDrawFeedAdLoad(List<DoNewsExpressDrawFeedAd> doNewsDrawFeedAds, int adFrom) {//获取数据成功
                if (doNewsDrawFeedAds == null || doNewsDrawFeedAds.isEmpty()) {
                    Log.d(TAG, "ad is null ");
                    return;
                }
                countRenderNum = 0;
                Log.d(TAG, "获取数据成功：返回数据" + doNewsDrawFeedAds.size());
                List<DoNewsExpressDrawFeedAd> dataList = new ArrayList<>();

                for (DoNewsExpressDrawFeedAd ad : doNewsDrawFeedAds) {
                    //头条draw广告

                    ad.setVideoAdListener(new VideoOperationListener());
                    ad.setCanInterruptVideoPlay(true);
                    ad.setExpressInteractionListener(new DoNewsExpressDrawFeedAd.ExpressAdInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            SdkLogUtils.i(TAG, "TT广告点击了");
                        }

                        @Override
                        public void onAdShow() {
                            SdkLogUtils.i(TAG, "TT广告开始展示");
                        }

                        @Override
                        public void onRenderFail(View view, String msg, int code) {
                            countRenderNum++;
                            if (listener != null || countRenderNum >= doNewsDrawFeedAds.size()) {

                            }
                            SdkLogUtils.i(TAG, "TT广告渲染失败：" + msg);
                        }

                        @Override
                        public void onRenderSuccess(View view, float width, float height) {
                            countRenderNum++;
                            if (listener != null || countRenderNum >= doNewsDrawFeedAds.size()) {

                            }
                            SdkLogUtils.i(TAG, "TT渲染成功");

                        }
                    });
                    //此方法 头条SDK会调用 快手渠道则没有  接入方也可以不设置此方法。
                    if (adFrom == 3) {
                        ad.render();
                    }
                    dataList.add(ad);
                    if (dataList.size() == doNewsDrawFeedAds.size()) {

                    }

                    // adFrom ==16,直客
                }
            }
        });
    }


    private class VideoOperationListener implements DoNewsExpressDrawFeedAd.ExpressVideoAdListener {
        @Override
        public void onVideoLoad() {
            SdkLogUtils.i(TAG, "视频缓存成功");
        }

        @Override
        public void onVideoError(int errorCode, int extraCode) {
            SdkLogUtils.i(TAG, "视频发生错误：");
        }

        @Override
        public void onVideoAdStartPlay() {
            SdkLogUtils.i(TAG, "视频开始播放：");
        }

        @Override
        public void onVideoAdPaused() {
            SdkLogUtils.i(TAG, "视频开始暂停：");
        }

        @Override
        public void onVideoAdContinuePlay() {
            SdkLogUtils.i(TAG, "视频开始继续播放：");
        }

        @Override
        public void onProgressUpdate(long current, long duration) {
            SdkLogUtils.i(TAG, "视频加载进度：" + current);
        }

        @Override
        public void onVideoAdComplete() {
            SdkLogUtils.i(TAG, "视频播放完成：");
        }

        @Override
        public void onClickRetry() {
            SdkLogUtils.i(TAG, "视频重新播放");
        }
    }
}
