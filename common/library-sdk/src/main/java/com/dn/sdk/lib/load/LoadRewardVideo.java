package com.dn.sdk.lib.load;

import android.app.Activity;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.dn.sdk.AdLoadManager;
import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.dialog.LoadingDialog;
import com.dn.sdk.lib.SdkManager;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.utils.SdkLogUtils;

import java.util.LinkedList;

/**
 * @author by SnowDragon
 * Date on 2021/1/19
 * Description:
 */
public class LoadRewardVideo {

    private final FragmentActivity activity;
    private final boolean isShowLoading;
    private final RequestInfo requestInfo;
    private final AdVideoListener videoListener;

    LinkedList<AdConfigBean.AdID> adIdS;
    LoadingDialog loadingDialog = null;

    public LoadRewardVideo(FragmentActivity activity, boolean isShowLoading,
                           RequestInfo requestInfo, AdVideoListener videoListener) {

        this.activity = activity;
        this.isShowLoading = isShowLoading;
        this.requestInfo = requestInfo;
        this.videoListener = videoListener;
        requestInfo.adType = AdType.REWARD_VIDEO;

    }

    public void loadAd() {

        adIdS = AdConfigSupply.getInstance().getAdIdList(requestInfo.adType);
        loadRewardVideo();

        if (isShowLoading) {
            loadingDialog = new LoadingDialog();

            //默认是6秒
            loadingDialog.setLoadingTime(6)
                    .setBackgroundDim(false)
                    .setCloseListener(() -> {
                        //弹窗关闭监听
                    });
            //展示弹窗
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(loadingDialog, "ad_loading")
                    .commitAllowingStateLoss();

        }
    }

    private void loadRewardVideo() {
        //循环加载结束
        if (adIdS.isEmpty()) {
            if (loadingDialog != null) {
                loadingDialog.dismissCusDialog();
            }

            if (activity != null) {
                Toast.makeText(activity.getApplicationContext(), "视频加载失败，请重试！", Toast.LENGTH_SHORT).show();
            }

            if (videoListener != null) {
                videoListener.onError(0, "");
            }
            return;
        }

        AdConfigBean.AdID adID = adIdS.poll();

        AdConfigSupply.getInstance().wrapperRequestInfo(adID, requestInfo);

        SdkLogUtils.i(SdkLogUtils.TAG, "  --SdkType: " + requestInfo.getSdkType().DESCRIPTION);
        SdkManager.getInstance().getAdController(requestInfo.getSdkType()).loadVideo(activity, requestInfo, new AdVideoListener() {


            @Override
            public void onAdShow() {

                if (loadingDialog != null) {
                    loadingDialog.dismissCusDialog();
                }
                if (videoListener != null) {
                    videoListener.onAdShow();
                }
                ACache.getInstance().registerRewardVideoListener(this);
            }

            @Override
            public void onAdClose() {
                SdkLogUtils.i(SdkLogUtils.TAG, " -------adClose");
                if (videoListener != null) {
                    videoListener.onAdClose();
                }
                AdConfigSupply.getInstance().setVideoUpPlayTime(System.currentTimeMillis());

                //关闭的时候,缓存下一条数据,如果缓存开启切缓存为null的时候，缓存一条
                if (AdConfigSupply.getInstance().usePreLoadPlayVideo() && !ACache.getInstance().hasVideoNativeCache()) {
                    AdLoadManager.getInstance()
                            .cacheRewardVideo(activity, new RequestInfo(AdIdConfig.REWARD_VIDEO_ID), null);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                requestInfo.usePassId = false;
                loadRewardVideo();
            }

            @Override
            public void videoComplete(Activity activity) {
                SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》videoComplete ");
                if (videoListener != null) {
                    videoListener.videoComplete(activity);
                }
                if (activity != null && AdConfigSupply.getInstance().isShowInterstitial()) {
                    AdLoadManager.getInstance()
                            .loadInterstitial(activity, new RequestInfo(AdIdConfig.INTERSTITIAL_ID), null);
                }
            }

            @Override
            public void onRewardVerify(boolean rewardVerify) {
                SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》onRewardVerify " + rewardVerify);
            }

            @Override
            public void videoCoolDownIng() {
                SdkLogUtils.i(SdkLogUtils.TAG, "  -------》》》videoCoolDownIng ");
            }
        });
    }
}
