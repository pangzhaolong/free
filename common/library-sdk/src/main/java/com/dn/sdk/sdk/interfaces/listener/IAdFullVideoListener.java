package com.dn.sdk.sdk.interfaces.listener;


import com.dn.sdk.sdk.interfaces.listener.preload.IAdErrorListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdVideoLoadListener;
import com.dn.sdk.sdk.interfaces.listener.show.IAdFullVideoShowListener;

/**
 * 全屏广告监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 15:02
 */
public interface IAdFullVideoListener extends IAdVideoLoadListener, IAdFullVideoShowListener, IAdErrorListener {
}
