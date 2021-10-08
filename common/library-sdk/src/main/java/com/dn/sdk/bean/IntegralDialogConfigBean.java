package com.dn.sdk.bean;

import com.donews.common.contract.BaseCustomViewModel;

/**
 * @author by SnowDragon
 * Date on 2021/4/12
 * Description:
 */
public class IntegralDialogConfigBean extends BaseCustomViewModel {

    /**
     * percent : 10
     * redPacketTime : 10
     * screenTime : 10
     */

    private int percent;
    private int redPacketTime;
    private int interstitialTime;

    /**
     * 引用退到后台，间隔splashBackgroundIntervalTime 出现SplashActivity，单位：秒
     * */
    private int splashBackgroundIntervalTime;

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getRedPacketTime() {
        return redPacketTime;
    }

    public void setRedPacketTime(int redPacketTime) {
        this.redPacketTime = redPacketTime;
    }

    public int getInterstitialTime() {
        return interstitialTime;
    }

    public void setInterstitialTime(int interstitialTime) {
        this.interstitialTime = interstitialTime;
    }

    public int getSplashBackgroundIntervalTime() {
        return splashBackgroundIntervalTime;
    }

    public void setSplashBackgroundIntervalTime(int splashBackgroundIntervalTime) {
        this.splashBackgroundIntervalTime = splashBackgroundIntervalTime;
    }

    @Override
    public String toString() {
        return "{" +
                "\"percent\":" + percent +
                ", \"redPacketTime\":" + redPacketTime +
                ", \"interstitialTime\":" + interstitialTime +
                '}';
    }
}
