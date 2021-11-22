package com.module.lottery.utils;

import com.dn.sdk.sdk.interfaces.view.PreloadVideoView;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 17:31
 */
public class  LotteryPreloadVideoView   {
    public PreloadVideoView getPreloadVideoView() {
        return mPreloadVideoView;
    }
    public void setPreloadVideoView(PreloadVideoView mPreloadVideoView) {
        this.mPreloadVideoView = mPreloadVideoView;
    }

    PreloadVideoView mPreloadVideoView;


    private IAdStateListener iAdStateListener;

    public  interface IAdStateListener {
        public void onRewardAdShow();
        public void onRewardedClosed();
        public void onRewardVerify(boolean result);
        public void onRewardVideoComplete();
    }

    public IAdStateListener getAdStateListener() {
        return iAdStateListener;
    }

    public void setAdStateListener(IAdStateListener iAdStateListener) {
        this.iAdStateListener = iAdStateListener;
    }

}
