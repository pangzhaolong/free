package com.dn.sdk.sdk.tt.helper;

import android.app.Activity;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.AdmobNativeAdOptions;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTSettingConfigCallback;
import com.bytedance.msdk.api.TTVideoOption;
import com.dn.admediation.csj.bean.DnTTUnifiedNativeAd;
import com.dn.admediation.csj.listener.DnTTNativeAd;
import com.dn.admediation.csj.listener.DnTTNativeAdLoadCallback;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeListener;
import com.dn.sdk.sdk.tt.utils.UIUtils;
import com.dn.sdk.sdk.tt.utils.VideoOptionUtil;
import com.donews.utilslibrary.base.UtilsConfig;

import java.util.List;

/**
 * 信息流广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 18:17
 */
public class TTAdNativeLoadHelper {

    private Activity mActivity;
    private RequestInfo mRequestInfo;
    private IAdNativeListener mListener;

    private DnTTUnifiedNativeAd mDnTTUnifiedNativeAd;

    private final TTSettingConfigCallback mConfigCallback = new TTSettingConfigCallback() {
        @Override
        public void configLoad() {
            load();
        }
    };

    public void loadAd(Activity activity, RequestInfo requestInfo, IAdNativeListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;
        if (TTMediationAdSdk.configLoadSuccess()) {
            load();
        } else {
            TTMediationAdSdk.registerConfigCallback(mConfigCallback);
        }

    }

    private void load() {
        mDnTTUnifiedNativeAd = new DnTTUnifiedNativeAd(mActivity, mRequestInfo.getAdId());
        //视频声音控制设置
        TTVideoOption videoOption = VideoOptionUtil.getTTVideoOption();

        AdmobNativeAdOptions admobNativeAdOptions = new AdmobNativeAdOptions();
        admobNativeAdOptions.setAdChoicesPlacement(
                AdmobNativeAdOptions.ADCHOICES_TOP_RIGHT)//设置广告小标默认情况下，广告选择叠加层会显示在右上角。
                .setRequestMultipleImages(true)//素材中可能包含多张图片，如果此值设置为true， 则表示需要展示多张图片，此值设置为 false（默认），仅提供第一张图片。
                .setReturnUrlsForImageAssets(true);//设置为true，SDK会仅提供Uri字段的值，允许自行决定是否下载实际图片，同时不会提供Drawable字段的值

        // 针对Gdt Native自渲染广告，可以自定义gdt logo的布局参数。该参数可选,非必须。
        FrameLayout.LayoutParams gdtNativeAdLogoParams =
                new FrameLayout.LayoutParams(
                        UIUtils.dip2px(UtilsConfig.getApplication(), 40),
                        UIUtils.dip2px(UtilsConfig.getApplication(), 13),
                        Gravity.RIGHT | Gravity.TOP); // 例如，放在右上角

        /**
         * 创建feed广告请求类型参数AdSlot,具体参数含义参考文档
         * 备注
         * 1: 如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
         * 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
         */
        AdSlot adSlot = new AdSlot.Builder()
                .setTTVideoOption(videoOption)//视频声音相关的配置
                .setAdmobNativeAdOptions(admobNativeAdOptions)
                .setAdStyleType(
                        AdSlot.TYPE_NATIVE_AD)//必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot
                // .TYPE_NATIVE_AD：原生广告
                // 备注
                // 1:如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
                // 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
                .setImageAdSize((int) UIUtils.getScreenWidthDp(UtilsConfig.getApplication()),
                        340)// 必选参数 单位dp ，详情见上面备注解释
                .setAdCount(mRequestInfo.getNativeAdCount()) //请求广告数量为1到3条
                .setGdtNativeAdLogoParams(gdtNativeAdLogoParams) // 设置gdt logo布局参数。
                .build();

        //请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        /**
         * 注：每次加载信息流广告的时候需要新建一个TTUnifiedNativeAd，否则可能会出现广告填充问题
         * (例如：mTTAdNative = new TTUnifiedNativeAd(this, mAdUnitId);）
         */
        mDnTTUnifiedNativeAd.loadAd(adSlot, new DnTTNativeAdLoadCallback() {
            @Override
            public void onAdLoaded(List<DnTTNativeAd> ads) {

            }

            @Override
            public void onAdLoadedFial(AdError adError) {
                if (mListener != null) {
                    mListener.onLoadFail(adError.code, adError.toString());
                    mListener.onError(adError.code, adError.toString());
                }
            }
        });

    }

}
