package com.dn.sdk.lib.donews;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.dn.sdk.BuildConfig;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.listener.IAdNewsFeedListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.dn.sdk.widget.AdViewWrapper;
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
public class NewsFeedAdLoadManager {
    public static final String TAG = "NewsFeedAdLoadManager";

    public void loadNewsFeedTemplate(Activity activity, RequestInfo requestInfo, boolean cacheAd, IAdCallBack iAdCallBack) {


        //如果是图文，强烈建议高度设置为0，自适应，如果一定要设置高度，建议接自渲染信息流接口
        // 如果为视频广告（SDK版本大于等于4.7），也建议设置为0，如果设置具体高度，有可能会显示不全，影响曝光
        //模块高（屏幕宽度-左右种间距）  容器的布局文件建议为宽高都为wrap_content
        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                //广告位id
                .setPositionid(requestInfo.id)
                //申请广告的的宽 单位dp，必传参数
                .setExpressViewWidth(requestInfo.width)
                //申请广告的高 单位dp 必传参数
                .setExpressViewHeight(requestInfo.height)
                //广告数量是1，现阶段返回的是1
                .setAdCount(1)
                .build();
        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();

        SdkLogUtils.i(SdkLogUtils.TAG, " doNes  loadNewsFeedTemplate id :" + requestInfo.id);
        CountTrackImpl track = new CountTrackImpl(requestInfo);

        doNewsAdNative.onCreatTemplateAd(activity, doNewsAD, new DoNewsAdNative.DoNewsTemplateListener() {
            @Override
            public void onAdError(final String s) {//请求广告失败
                SdkLogUtils.i(SdkLogUtils.TAG, "loadNewsFeedTemplate  onAdError " + s);

                track.onLoadError();

                if (iAdCallBack != null) {
                    iAdCallBack.onError(s);
                }

            }

            @Override
            public void onADLoaded(List<View> list) {

                if (list == null || list.size() == 0) {
                    SdkLogUtils.E(TAG + "loadNewsFeedTemplate  onADLoaded but list is 0 ");
                    if (iAdCallBack != null) {
                        iAdCallBack.onError("loadNewsFeedTemplate  onADLoaded but list is 0");
                    }
                    return;
                }
                SdkLogUtils.i(SdkLogUtils.TAG, "----------loadNewsFeedTemplate：onADLoaded " + list.size());
                //现在只返回一条广告,可以直接写死
                if (cacheAd) {
                    ACache.getInstance().cacheFeedTemplate(list);
                } else {
                    requestInfo.container.removeAllViews();
                    requestInfo.container.addView(list.get(0));//添加广告
                }
            }

            @Override
            public void onNoAD(String s) {//没有广告
                Log.i(TAG, "----------loadNewsFeedTemplate onNoAD ：" + s);
            }


            @Override
            public void onAdClose() {//广告关闭
                Log.i(TAG, "----------loadNewsFeedTemplate onAdClose ：");
                track.onAdClose();
                if (iAdCallBack != null) {
                    iAdCallBack.onClose();
                }

                requestInfo.container.removeAllViews();
            }

            @Override
            public void onADExposure() {
                track.onShow();

                if (iAdCallBack != null) {
                    iAdCallBack.onShow();
                }
            }


            @Override
            public void onADClicked() {
                track.onClick();

            }

        });
    }

    /**
     * 自渲染信息流
     *
     * @param activity    activity
     * @param requestInfo 请求信息
     * @param listener
     */
    public void loadNewsFeedCustomRender(Activity activity, RequestInfo requestInfo, int layoutId, IAdNewsFeedListener listener) {

        if (BuildConfig.DEBUG) {
            SdkLogUtils.d(SdkLogUtils.TAG, " requestInfo id : " + requestInfo.id);
        }

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                //广告位
                .setPositionid(requestInfo.id)
                .setAdCount(requestInfo.adNum)
                .build();
        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        CountTrackImpl track = new CountTrackImpl(requestInfo);
        try {
            doNewsAdNative.onCreateAdInformation(activity, doNewsAD, new DoNewsAdNative.DoNewsNativesListener() {
                @Override
                public void OnFailed(String s) {//请求广告失败
                    if (listener != null) {
                        listener.onError(s);
                    }
                    Log.e(TAG, "loadNewsFeedCustomRender OnFailed : " + s);
                }

                @Override
                public void Success(List<DoNewsAdNativeData> list) {//请求信息流广告成功
                    if (list == null || list.size() == 0) {
                        return;
                    }

                    if (listener != null) {
                        listener.success(new AdViewWrapper().createViewByNewsFeedList(activity, layoutId, track, list));
                    }

                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e.getMessage());
            }

            Log.e(TAG, "loadNewsFeedCustomRender failed : " + e.getMessage());
        }

    }


}
