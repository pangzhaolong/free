package com.dn.sdk.bean.integral;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.donews.ads.mediation.v2.integral.DnIntegralAdListener;
import com.donews.ads.mediation.v2.integral.DnIntegralNativeAd;

import java.util.List;

public abstract class IntegralBean {
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
