package com.dn.sdk.sdk.interfaces.listener.preload;

/**
 * 广告加载监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:22
 */
public interface IAdLoadListener {

    /**
     * 加载成功
     */
    public void onLoad();

    /**
     * 加载广告失败
     *
     * @param code  失败code
     * @param error 错误信息
     */
    public void onLoadFail(int code, String error);

    /**
     * 加载超时
     */
    public void onLoadTimeout();
}
