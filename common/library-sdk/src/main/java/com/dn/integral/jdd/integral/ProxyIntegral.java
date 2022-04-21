package com.dn.integral.jdd.integral;

import com.donews.ads.mediation.v2.integral.DnIntegralNativeAd;

public class ProxyIntegral extends IntegralBean{



    DnIntegralNativeAd mDnIntegralNativeAd;


    public ProxyIntegral(DnIntegralNativeAd dnIntegralNativeAd) {
        super(dnIntegralNativeAd);

        mDnIntegralNativeAd=dnIntegralNativeAd;


    }

    @Override
    public String getAppName() {
        return mDnIntegralNativeAd.getAppName();
    }

    @Override
    public String getApkUrl() {
        return mDnIntegralNativeAd.getApkUrl();
    }

    @Override
    public String getPkName() {
        return mDnIntegralNativeAd.getPkName();
    }

    @Override
    public String getIcon() {
        return mDnIntegralNativeAd.getIcon();
    }

    @Override
    public int getPrice() {
        return mDnIntegralNativeAd.getPrice();
    }

    @Override
    public String getDeepLink() {
        return mDnIntegralNativeAd.getDeepLink();
    }

    @Override
    public String getDesc() {
        return mDnIntegralNativeAd.getDesc();
    }

    @Override
    public String getTaskType() {
        return mDnIntegralNativeAd.getTaskType();
    }

    @Override
    public String getWallRequestId() {
        return mDnIntegralNativeAd.getWallRequestId();
    }

    @Override
    public String getSourceRequestId() {
        return mDnIntegralNativeAd.getSourceRequestId();
    }

    @Override
    public String getSourcePlatform() {
        return mDnIntegralNativeAd.getSourcePlatform();
    }

    @Override
    public String getSourceAdType() {
        return mDnIntegralNativeAd.getSourceAdType();
    }


}
