package com.dn.sdk.api;

import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.utils.SdkLogUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author by SnowDragon
 * Date on 2020/12/17
 * Description:
 */
public class AdConfigSupply {
    private static final AdConfigSupply AD_CONFIG = new AdConfigSupply();
    static volatile boolean hasInit = false;

    public static AdConfigSupply getInstance() {
        return AD_CONFIG;
    }


    private AdConfigBean bean;

    public void setAdConfigBean(AdConfigBean bean) {
        this.bean = bean;
        videoCompleteShowInterstitial();
    }


    /**
     * 激励视频配置列表
     *
     * @return List<AdID></AdID>
     */
    public List<AdConfigBean.AdID> getFullVideoList() {
        if (bean == null) {
            return null;
        }
        return bean.fullVideo;
    }


    /**
     * 根据配置确定使用的广告id，类型
     *
     * @return string
     */
    public void wrapperRequestInfo(AdConfigBean.AdID adID, RequestInfo requestInfo) {

        //取不到服务端配置, 或者强制使用传递进来的广告id
        if (adID == null || requestInfo.usePassId) {
            return;
        }

        //使用配置中的广告id
        requestInfo.id = adID.id;
        requestInfo.sdkType = adID.getSdkType();

    }


    public String getDrawFeedId() {
        return null;
    }


    /**
     * 激励视频是否可以跳过
     *
     * @return true:可以跳过激励视频
     */
    public boolean isSkipVideo() {
        if (bean != null) {
            return bean.videoQuickSkip;
        }
        return false;
    }

    /**
     * true:表示播放激励视频
     *
     * @return boolean
     */
    public boolean isPlayVideo() {
        if (bean != null) {
            return bean.videoDisplay;
        }
        return true;
    }

    public boolean usePreLoadPlayVideo() {
        if (bean != null) {
            return bean.usePreload;
        }
        return true;
    }

    /**
     * 激励视频播放间隔
     *
     * @return 毫秒
     */
    public long getVideoPlayInterval() {
        if (bean != null) {
            return bean.videoInterval;
        }
        return 0;
    }

    private long videoUpPlayTime = 0;

    /**
     * 激励视频上一次播放时间
     *
     * @param videoUpPlayTime time:毫秒
     */
    public void setVideoUpPlayTime(long videoUpPlayTime) {
        this.videoUpPlayTime = videoUpPlayTime;

    }

    public long getVideoUpPlayTime() {
        return videoUpPlayTime;
    }

    /**
     * @param adType 广告类型
     * @return 广告id列表
     */
    public LinkedList<AdConfigBean.AdID> getAdIdList(AdType adType) {
        LinkedList<AdConfigBean.AdID> result = new LinkedList<>();
        List<AdConfigBean.AdID> adIdS = getAdIds(adType);

        //广告配置获取不到或者为null，使用传递进来的广告id
        if (adIdS == null) {
            result.add(null);
        } else {
            result.addAll(adIdS);
        }
        return result;
    }

    private List<AdConfigBean.AdID> getAdIds(AdType adType) {
        if (bean == null) {
            return null;
        }
        switch (adType) {
            case SPLASH:
                return bean.spread;
            case BANNER:
                return bean.banner;
            case INTERSTITIAL:
                return bean.interstitial;
            case NEWS_FEED_TEMPLATE:
                return bean.temp;
            case REWARD_VIDEO:
                return bean.video;
            case NEWS_FEED_CUSTOM_RENDER:
                return bean.self;
            default:
                return null;

        }
    }

    private SortedSet<Integer> interstitialRandomArr = new TreeSet();
    private int videoPlayTimes = 0;

    private void recordVideoPlayTimes() {
        videoPlayTimes += 1;
    }


    /**
     * 激励视频播放结束之后，出插屏广告
     */
    private void videoCompleteShowInterstitial() {
        videoPlayTimes = 0;
        interstitialRandomArr.clear();
        if (bean == null || !bean.videoInterstitial) {
            return;
        }

        //防止插屏的显示次数大于激励视频次数，造成死循环；
        // 如：在bean.videoNumb(10)次中显示bean.videoInterstitialTimes(20)次
        if (bean.videoInterstitialTimes >= bean.videoNumb) {
            for (int i = 0; i < bean.videoNumb; i++) {
                interstitialRandomArr.add(i);
            }

            return;
        }
        while (interstitialRandomArr.size() < bean.videoInterstitialTimes) {
            interstitialRandomArr.add(new Random().nextInt(bean.videoNumb));
        }

    }

    public boolean isShowInterstitial() {
        SdkLogUtils.i(SdkLogUtils.TAG, "  ---isShowInterstitial---- ");
        if (bean == null || !bean.videoInterstitial) {
            return false;
        }
        //视频播放bean.videoNumb次之后，重新循环
        if (videoPlayTimes > bean.videoNumb) {
            videoCompleteShowInterstitial();
        }
        boolean showInterstitial = interstitialRandomArr.contains(videoPlayTimes);

        //视频播放次数+1
        recordVideoPlayTimes();
        SdkLogUtils.i(SdkLogUtils.TAG, "  -----isShowInterstitial：：： " + showInterstitial);
        return showInterstitial;
    }
}
