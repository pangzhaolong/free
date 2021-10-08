package com.dn.sdk.listener;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public interface IAdCallBack {
    void onError(String error);

    void onShow();

    void onClose();
}
