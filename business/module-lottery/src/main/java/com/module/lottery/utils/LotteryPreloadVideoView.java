package com.module.lottery.utils;

import com.dn.sdk.bean.preload.PreloadRewardVideoAd;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 17:31
 */
public class LotteryPreloadVideoView {
    public PreloadRewardVideoAd getPreloadVideoView() {
        return mPreloadVideoView;
    }

    public void setPreloadVideoView(PreloadRewardVideoAd mPreloadVideoView) {
        this.mPreloadVideoView = mPreloadVideoView;
    }

    PreloadRewardVideoAd mPreloadVideoView;


    private IAdStateListener iAdStateListener;

    public interface IAdStateListener {
        public void onRewardAdShow();

        public void onRewardedClosed();

        public void onRewardVerify(boolean result);

        public void onRewardVideoComplete();

        public void onError(int code, String msg);
    }

    public IAdStateListener getAdStateListener() {
        return iAdStateListener;
    }

    public void setAdStateListener(IAdStateListener iAdStateListener) {
        this.iAdStateListener = iAdStateListener;
    }

}
