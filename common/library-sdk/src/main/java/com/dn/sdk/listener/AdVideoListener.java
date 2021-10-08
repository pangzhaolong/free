package com.dn.sdk.listener;

import android.app.Activity;

/**
 * @author by SnowDragon
 * Date on 2020/11/19
 * Description:
 */
public abstract class AdVideoListener {

    public abstract void onAdShow();

    public abstract void onAdClose();

    public abstract void onError(int errorCode, String errorMsg);

    /**
     * 视频播放完成
     */
    public void videoComplete(Activity activity) {
    }


    /**
     * 激励视冷却中
     */
    public void videoCoolDownIng() {
    }

    /**
     * @param rewardVerify true:激励完成
     */
    public void onRewardVerify(boolean rewardVerify) {

    }


}
