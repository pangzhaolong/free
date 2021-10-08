package com.dn.sdk.count;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public interface ITrack {

    /**
     * 统计广告点击
     */
    void onClick();

    /**
     * 统计广告展示
     */
    void onShow();

    /**
     * 统计广告关闭
     */
    void onAdClose();

    /**
     * 统计广告加载失败
     */
    void onLoadError();

    /**
     * 统计广告曝光成功
     */
    void onADExposure();

    /**
     * @param 是否给予奖励
     */
    void onRewardVerify(boolean b);


    /**
     * 视频播放完成
     */
    void onVideoComplete();


    /**
     * app下载完成
     */
    void downloadFinished();

}
