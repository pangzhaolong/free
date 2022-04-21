package com.dn.integral.jdd.integral;

import com.donews.ads.mediation.v2.integral.DnIntegralNativeAd;

import java.io.Serializable;

public abstract class IntegralBean implements Serializable {
    public DnIntegralNativeAd getDnIntegralNativeAd() {
        return mDnIntegralNativeAd;
    }

    public void setDnIntegralNativeAd(DnIntegralNativeAd mDnIntegralNativeAd) {
        this.mDnIntegralNativeAd = mDnIntegralNativeAd;
    }

    DnIntegralNativeAd mDnIntegralNativeAd;

    public IntegralBean(DnIntegralNativeAd dnIntegralNativeAd) {
        mDnIntegralNativeAd = dnIntegralNativeAd;
    }


    public abstract   String getAppName();

    public abstract    String getApkUrl();

    public abstract    String getPkName();

    public abstract   String getIcon();

    public abstract   int getPrice();

    public abstract  String getDeepLink();

    public abstract    String getDesc();

    public abstract  String getTaskType();

    public abstract  String getWallRequestId();

    public abstract  String getSourceRequestId();

    public abstract   String getSourcePlatform();

    public abstract  String getSourceAdType();


}
