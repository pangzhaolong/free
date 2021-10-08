package com.dn.sdk.test;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dn.sdk.AdLoadManager;
import com.dn.sdk.R;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.databinding.FragmentTestSdkBinding;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.base.base.DataBindingVars;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.utils.DensityUtils;

/**
 * @author by SnowDragon
 * Date on 2021/1/18
 * Description:
 */
@Route(path = RouterFragmentPath.TestSdk.PAGER_TEST_AD_SDK)
public class TestAdSdkFragment extends MvvmLazyLiveDataFragment<FragmentTestSdkBinding, BaseLiveDataViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_test_sdk;
    }

    private VideoNative videoNative;

    /**
     *
     */
    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        //激励视频,RequestInfo，传入广告id即可
        mDataBinding.btnLoad.setOnClickListener(v ->
                AdLoadManager.getInstance()
                        .loadRewardVideo(getActivity(),
                                new RequestInfo(AdIdConfig.REWARD_VIDEO_ID),
                                new AdVideoListener() {
                                    @Override
                                    public void onAdShow() {
                                    }

                                    @Override
                                    public void onAdClose() {
                                        SdkLogUtils.i(SdkLogUtils.TAG, "  onAdClose ");
                                    }

                                    @Override
                                    public void onError(int errorCode, String errorMsg) {

                                    }

                                    @Override
                                    public void videoComplete(Activity activity) {

                                        if (activity != null) {
                                            AdLoadManager.getInstance().loadInterstitial(activity, new RequestInfo(AdIdConfig.INTERSTITIAL_ID));
                                        }

                                    }

                                    @Override
                                    public void videoCoolDownIng() {

                                    }
                                }));


        //激励视频预加载
        mDataBinding.btnRewardVideoPre.setOnClickListener(v -> {

            videoNative = AdLoadManager.getInstance().preLoadRewardVideo(getActivity(), new RequestInfo(AdIdConfig.REWARD_VIDEO_ID), null);
        });
        mDataBinding.btnRewardVideoShow.setOnClickListener(v -> {
            if (videoNative != null && videoNative.isReady()) {
                videoNative.register(new AdVideoListener() {
                    @Override
                    public void onAdShow() {
                        Log.i("chyy", " pre register onAdshow ");
                    }

                    @Override
                    public void onAdClose() {
                        SdkLogUtils.i(SdkLogUtils.TAG, "  onAdClose ");
                        Log.i("chyy", " pre register onAdClose ");
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Log.i("chyy", " pre register errorMsg " + errorCode + "   " + errorMsg);
                    }

                    @Override
                    public void videoCoolDownIng() {

                    }
                });
                videoNative.showRewardVideoAd(getActivity());
            }
        });

        //加载信息流模板 这里的宽搞为dp
        DisplayMetrics outMetrics = new DisplayMetrics();
        // getActivity().getDisplay().getRealMetrics(outMetrics);

        float widthDp = DensityUtils.px2dp(getActivity(), 1080);
        float heightDp = 0;

        mDataBinding.btnLoadNewsFeedTemplate.setOnClickListener(v -> {
            mDataBinding.container.removeAllViews();
            AdLoadManager.getInstance().loadNewsFeedTemplate(getActivity(),
                    new RequestInfo(AdIdConfig.NEWS_FEED_TEMPLATE_ID, widthDp, heightDp, mDataBinding.container),
                    null);
        });

        //自渲染广告信息流
        mDataBinding.btnLoadNewsFeedRender.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NewsFeedCustomerRenderActivity.class));

        });
        mDataBinding.btnInterstitial.setOnClickListener(v ->
                AdLoadManager.getInstance().loadInterstitial(getActivity(), new RequestInfo(AdIdConfig.INTERSTITIAL_ID))
        );

        mDataBinding.btnBanner.setOnClickListener(v -> {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            //模块高（屏幕宽度-左右种间距）  容器的布局文件建议为宽高都为wrap_content
            int widthDP = (int) DensityUtils.px2dp(getActivity(), widthPixels);
            RequestInfo info = new RequestInfo(AdIdConfig.INTERSTITIAL_ID);
            info.width = widthDP;
            info.container = mDataBinding.container;

            AdLoadManager.getInstance().loadBanner(getActivity(), info, null);
        });


        mDataBinding.btnNewUser.setOnClickListener(v -> {
            AdLoadManager.getInstance().loadInterstitial(getActivity(), new RequestInfo(AdIdConfig.INTERSTITIAL_ID, 300, 800));
        });

    }


    @Override
    public DataBindingVars getBindingVariable() {
        return null;
    }


}
