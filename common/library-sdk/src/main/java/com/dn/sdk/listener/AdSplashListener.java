package com.dn.sdk.listener;

/**
 * @author by SnowDragon
 * Date on 2020/11/19
 * Description:
 */
public interface AdSplashListener {

    public void onNoAD(String s);


    public void onClicked();


    public void onShow();


    public void onPresent();


    public void onADDismissed();


    public void extendExtra(String s);
}
