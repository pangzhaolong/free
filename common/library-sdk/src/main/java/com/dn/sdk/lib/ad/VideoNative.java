package com.dn.sdk.lib.ad;

import android.app.Activity;

import com.dn.sdk.lib.SDKType;
import com.dn.sdk.listener.AdVideoListener;
import com.donews.b.main.DoNewsAdNative;

/**
 * @author by SnowDragon
 * Date on 2021/1/18
 * Description:
 */
public class VideoNative {

    private DoNewsAdNative doNewsAdNative;
    private SDKType sdkType;
    boolean isReady = false;

    public VideoNative() {
    }



    public VideoNative(DoNewsAdNative doNewsAdNative, SDKType sdkType) {
        this.doNewsAdNative = doNewsAdNative;
        this.sdkType = sdkType;

    }



    public void setDoNewsAdNative(DoNewsAdNative doNewsAdNative, SDKType sdkType) {
        this.doNewsAdNative = doNewsAdNative;
        this.sdkType = sdkType;
    }



    /**
     * @param activity
     */
    public void showRewardVideoAd(Activity activity) {
        switch (sdkType) {
            case DO_NEWS:
                if (doNewsAdNative != null) {
                    doNewsAdNative.showRewardAd();
                }
                break;
            case YOU_LIANG_BAO:
            case ADCDN:
                break;
            default:
        }
    }

    private AdVideoListener adVideoListener;

    public void register(AdVideoListener adVideoListener) {
        this.adVideoListener = adVideoListener;
    }

    public AdVideoListener getAdVideoListener() {
        return adVideoListener;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public SDKType getSdkType() {
        return sdkType;
    }


    public void wrapper(VideoNative videoNative) {
        videoNative.sdkType = sdkType;
        switch (sdkType) {
            case ADCDN:
            case DO_NEWS:
                videoNative.setDoNewsAdNative(doNewsAdNative, sdkType);
                break;
            case YOU_LIANG_BAO:
                break;
            default:
        }
    }
}
