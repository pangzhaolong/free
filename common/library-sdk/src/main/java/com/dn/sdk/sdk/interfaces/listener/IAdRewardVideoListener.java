package com.dn.sdk.sdk.interfaces.listener;


import com.dn.sdk.sdk.interfaces.listener.preload.IAdErrorListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdVideoLoadListener;
import com.dn.sdk.sdk.interfaces.listener.show.IAdRewardVideoShowListener;

/**
 * 激励视频监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:46
 */
public interface IAdRewardVideoListener extends IAdVideoLoadListener, IAdRewardVideoShowListener, IAdErrorListener {
}
