package com.dn.sdk.bean;

import android.view.ViewGroup;

import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SDKType;

/**
 * @author by SnowDragon
 * Date on 2020/11/19
 * Description:
 */
public class RequestInfo {

    /**
     * 广告位
     */
    public String id;

    //请求时唯一Id
    public String requestId;

    public float width;
    public float height;
    /**
     * 广告类型
     */
    public AdType adType;
    /**
     * 广告容器
     */
    public ViewGroup container;
    public int adNum;

    public SDKType sdkType = SDKType.DO_NEWS;

    /**
     * 使用传递进来的广告id，默认false
     */
    public boolean usePassId;

    public SDKType getSdkType() {
        return sdkType;
    }

    public boolean preLoad = false;


    public RequestInfo(String id) {
        this.id = id;
    }

    public RequestInfo(String id, float width, float height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public RequestInfo(String id, float width, float height, ViewGroup container) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.container = container;
    }

    public RequestInfo(String id, float width, float height, ViewGroup container, int adNum) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.container = container;
        this.adNum = adNum;
    }
}
