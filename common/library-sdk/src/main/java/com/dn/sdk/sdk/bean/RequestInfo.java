package com.dn.sdk.sdk.bean;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dn.sdk.sdk.platform.IPlatform;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告请求信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:47
 */
public class RequestInfo {

    /** 广告加载器 */
    private IPlatform mPlatform;

    /** 请求广告类型 */
    private AdType mAdType;

    /** 广告app id */
    private String mAppId;

    /** 广告位对应的key 字符串 */
    private String mAppIdKey;

    /** 广告位id */
    private String mAdId;

    /** 保底代码位id */
    private String mMinimumCodeId;

    /** 广告展示容器 */
    private ViewGroup mContainer;

    /** 广告宽度 */
    private int mWidth;

    /** 广告高度 */
    private int mHeight;

    /** 是否预加载 */
    private boolean preload;

    /** 1竖屏，2横屏，默认竖屏 */
    private int mOrientation = 1;
    /** 激励奖励 */
    private String mRewardName;
    /** 激励数量 */
    private int mRewardAmount;

    /** 激励视频 自定义数据 */
    private Map<String, String> mCustomData = new HashMap<>();

    /** 信息流请求数据条数 */
    private int mNativeAdCount;

    /** 公共信息 无需手动传递 */
    private String mChannel;
    private String mOaid;
    private String mUserId;


    public IPlatform getPlatform() {
        return mPlatform;
    }

    public void setPlatform(IPlatform platform) {
        mPlatform = platform;
    }

    public AdType getAdType() {
        return mAdType;
    }

    public void setAdType(AdType adType) {
        mAdType = adType;
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getAdId() {
        return mAdId;
    }

    public void setAdId(String adId) {
        this.mAdId = adId;
    }

    public String getAppIdKey() {
        return mAppIdKey;
    }

    public void setAppIdKey(String appIdKey) {
        mAppIdKey = appIdKey;
    }

    public String getMinimumCodeId() {
        return mMinimumCodeId;
    }

    public void setMinimumCodeId(String minimumCodeId) {
        this.mMinimumCodeId = minimumCodeId;
    }

    public ViewGroup getContainer() {
        return mContainer;
    }

    public void setContainer(ViewGroup container) {
        mContainer = container;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public String getRewardName() {
        return mRewardName;
    }

    public void setRewardName(String rewardName) {
        mRewardName = rewardName;
    }

    public int getRewardAmount() {
        return mRewardAmount;
    }

    public void setRewardAmount(int rewardAmount) {
        mRewardAmount = rewardAmount;
    }

    public Map<String, String> getCustomData() {
        return mCustomData;
    }

    public void setCustomData(Map<String, String> customData) {
        mCustomData = customData;
    }

    public int getNativeAdCount() {
        return mNativeAdCount;
    }

    public void setNativeAdCount(int nativeAdCount) {
        mNativeAdCount = nativeAdCount;
    }

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        mChannel = channel;
    }

    public String getOaid() {
        return mOaid;
    }

    public void setOaid(String oaid) {
        mOaid = oaid;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public boolean isPreload() {
        return preload;
    }

    public void setPreload(boolean preload) {
        this.preload = preload;
    }


    @NonNull
    @Override
    public String toString() {
        return "RequestInfo{" +
                "mPlatform=" + mPlatform +
                ", mAdType=" + mAdType +
                ", mAppId='" + mAppId + '\'' +
                ", mAdId='" + mAdId + '\'' +
                ", mMinimumCodeId='" + mMinimumCodeId + '\'' +
                ", mContainer=" + mContainer +
                ", mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", preload=" + preload +
                ", mOrientation=" + mOrientation +
                ", mRewardName='" + mRewardName + '\'' +
                ", mRewardAmount=" + mRewardAmount +
                ", mCustomData=" + mCustomData +
                ", mNativeAdCount=" + mNativeAdCount +
                ", mChannel='" + mChannel + '\'' +
                ", mOaid='" + mOaid + '\'' +
                ", mUserId='" + mUserId + '\'' +
                '}';
    }
}
