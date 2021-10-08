package com.dn.sdk.lib;

import com.dn.sdk.lib.donews.DoNewsController;

/**
 * @author by SnowDragon
 * Date on 2021/1/15
 * Description:
 */
public class SdkManager {

    public static final SdkManager sInstance = new SdkManager();

    private SdkManager() {
    }

    public static SdkManager getInstance() {
        return sInstance;
    }

    public IAdController getAdController(SDKType type) {
        return new DoNewsController();
    }

}
