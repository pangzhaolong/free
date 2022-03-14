package com.donews.base.appdialog.permission;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public interface PermissionListener {
    /**
     * 授权成功
     */
    void onSuccess();

    /**
     * 授权失败
     */
    void onFail();
}
