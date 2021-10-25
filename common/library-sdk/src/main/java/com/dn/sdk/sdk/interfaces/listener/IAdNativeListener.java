package com.dn.sdk.sdk.interfaces.listener;


import com.dn.sdk.sdk.interfaces.listener.preload.IAdErrorListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdNativeLoadListener;

/**
 * 原生信息流监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 15:55
 */
public interface IAdNativeListener extends IAdNativeLoadListener, IAdNativeDislikeListener,
        IAdNativeVideoListener, IAdErrorListener {
    /**
     * 点击回调事件
     */
    void onAdClick();

    /**
     * 展示回调事件
     */
    void onAdShow();

    /** 关闭信息流 */
    void onAdClose();
}
