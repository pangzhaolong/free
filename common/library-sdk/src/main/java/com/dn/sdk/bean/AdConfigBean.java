package com.dn.sdk.bean;

import com.dn.sdk.lib.SDKType;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/12/17
 * Description:
 */
public class AdConfigBean {
    /*"self": 1231,
            "spread": 1231,
            "temp": 1231,
            "video": 1312,
            "videoDisplay": true,
            "videoInterval": 8,
            "videoQuickSkip": true*/

    /**
     * 开屏
     */
    public List<AdID> spread;
    /**
     * 自渲染
     */
    public List<AdID> self;
    /**
     * 模板广告
     */
    public List<AdID> temp;
    /**
     * 激励视频
     */
    public List<AdID> video;

    /**
     * 自渲染
     */
    public List<AdID> fullVideo;


    /**
     * 插屏广告
     */
    public List<AdID> interstitial;


    /**
     * banner广告
     */
    public List<AdID> banner;
    /**
     * 间隔
     */
    public int videoInterval;
    /**
     * 是否播放激励视频
     */
    public boolean videoDisplay;
    /**
     * 激励视频跳过
     */
    public boolean videoQuickSkip;

    /**
     * 是否激励视频播放完成显示，插屏
     */
    public boolean videoInterstitial;

    /**
     * 激励视频播放完成后，插屏展示次数
     */
    public int videoInterstitialTimes;

    /**
     * 在videoNumb激励视频播放次数中显示videoInterstitialTime次插屏
     */
    public int videoNumb;

    /**
     * 是否使用预加载
     */

    public boolean usePreload;

    public static class AdID {
        public String id;
        private int sdkType = 1;
        public String name;

        public AdID(int sdkType) {
            this.sdkType = sdkType;
        }

        public AdID(String id, int sdkType) {
            this.id = id;
            this.sdkType = sdkType;
        }

        public SDKType getSdkType() {
            if (sdkType == 1) {
                return SDKType.DO_NEWS;
            } else if (sdkType == 2) {
                return SDKType.YOU_LIANG_BAO;
            } else if (sdkType == 3) {
                return SDKType.ADCDN;
            }

            return SDKType.DO_NEWS;
        }

        @Override
        public String toString() {
            return "AdID{" +
                    "id='" + id + '\'' +
                    ", sdkType=" + sdkType +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AdConfigBean{" +
                "spread=" + spread +
                ", self=" + self +
                ", temp=" + temp +
                ", video=" + video +
                ", fullVideo=" + fullVideo +
                ", interstitial=" + interstitial +
                ", banner=" + banner +
                ", videoInterval=" + videoInterval +
                ", videoDisplay=" + videoDisplay +
                ", videoQuickSkip=" + videoQuickSkip +
                ", videoInterstitial=" + videoInterstitial +
                ", videoInterstitialTimes=" + videoInterstitialTimes +
                ", videoNumb=" + videoNumb +
                ",usePreload=" + usePreload +
                '}';
    }
}
