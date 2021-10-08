package com.dn.sdk.listener.impl;

import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.info.NativeAdListener;

/**
 * @author by SnowDragon
 * Date on 2020/11/24
 * Description:
 */
public class NativeAdListenerImpl implements NativeAdListener {
    public static final String TAG = "NativeAdListenerImpl";
    CountTrackImpl track = null;


    public NativeAdListenerImpl(CountTrackImpl track){
        this.track = track;
    }
    @Override
    public void onADExposed() {
        SdkLogUtils.i(TAG, " onADExposed");
        track.onShow();
    }

    @Override
    public void onADClicked() {
        track.onClick();
    }

    @Override
    public void onADError(String s) {
        track.onLoadError();
        SdkLogUtils.i(TAG, " onADError --> " + s);
    }
}
