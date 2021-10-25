package com.dn.sdk.sdk.interfaces.listener.preload;


/**
 * 视频广告加载监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:20
 */
public interface IAdVideoLoadListener extends IAdLoadListener {


    /**
     * 视频加载成功
     */
    void onLoadCached();
}
