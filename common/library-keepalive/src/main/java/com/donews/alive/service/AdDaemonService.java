package com.donews.alive.service;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.keepalive.daemon.core.notification.NotifyResidentService;

/**
 * @author by SnowDragon
 * Date on 2020/11/27
 * Description:
 */
public class AdDaemonService extends NotifyResidentService {
    public static final String TAG = "AdDaemonService";

    @Override
    public void doStartCommand(Intent intent, int i, int i1) {

    }

    @Override
    public void doStart() {
      //  AdLoadManager.getInstance().init(getApplication(), true);
        Log.i(TAG, " do Start --->>>>  ");
        //时间改变
/*        registerTimeChangeReceiver();
        //屏幕状态改变
        registerScreenWitchReceiver();
        //电池电量变化
        registerBatteryReceiver();
        //引用安装，卸载广播
        registerPackageReceiver();
        //wifi状态改变广播
        registerWifiReceiver();
        //set选项广播
        registerSetReceiver();*/



    }

/*
    private void registerTimeChangeReceiver() {


        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

        registerReceiver(new TimeReceiver(), filter);
    }
*/


    /**
     * 屏幕监听广播
     */
/*    private void registerScreenWitchReceiver() {

        //锁屏广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

       // filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        registerReceiver(new ScreenSwitchReceiver(), filter);


    }*/

    /**
     * 电池广播
     */
/*    private void registerBatteryReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        registerReceiver(new BatteryReceiver(), filter);
    }*/

    /**
     * 注册包名监听广播
     */
/*    private void registerPackageReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");

        registerReceiver(new PackageReceiver(), filter);
    }*/

    /**
     * 注册包名监听广播
     */
/*    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        registerReceiver(new WifiStatusReceiver(), filter);
    }*/

    /**
     * 注册set监听广播
     */
/*    private void registerSetReceiver() {
        IntentFilter filter = new IntentFilter();
        for (String action : SystemSetReceiver.STATE_INTENTS) {
            filter.addAction(action);
        }

        registerReceiver(new SystemSetReceiver(), filter);
    }*/



}
