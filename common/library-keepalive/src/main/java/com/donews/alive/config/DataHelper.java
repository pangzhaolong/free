package com.donews.alive.config;

import android.util.Log;


import com.donews.alive.bean.AppOutBean;
import com.donews.utilslibrary.utils.DateTimeUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

/**
 * @author by SnowDragon
 * Date on 2020/12/4
 * Description:
 */
public class DataHelper {

    private static MMKV mmkv;

    private DataHelper() {
        mmkv = MMKV.mmkvWithID("out_ad_kv", MMKV.MULTI_PROCESS_MODE);
    }

    public static DataHelper getInstance() {
        return MmkvHolder.INSTANCE;
    }

    private static class MmkvHolder {
        private static final DataHelper INSTANCE = new DataHelper();
    }

    /*
     * 手机锁屏状态
     * */
    public static final String SCREEN_IS_LOCKED = "screen_locked";

    public void setLockScreenStatus(boolean status) {
        mmkv.putBoolean(SCREEN_IS_LOCKED, status).commit();
    }

    public boolean screenIsLocked() {
        return mmkv.getBoolean(SCREEN_IS_LOCKED, false);
    }


    public static final String APP_DIALOG_UP_SHOW_TIME = "app_up_show_time";

    public void setAppDialogUpShowTime() {
        mmkv.putLong(APP_DIALOG_UP_SHOW_TIME, System.currentTimeMillis()).commit();
    }

    /**
     * 启动弹窗，需要3-8秒，第一次启动的弹窗，在关闭后，刚好第二个弹窗出来（启动延时），用户体验不好
     *
     * @return :true 表示弹窗有效
     */
    public boolean isValidDialog() {
        return (System.currentTimeMillis() - mmkv.getLong(APP_DIALOG_UP_SHOW_TIME, 0)) > 10 * 1000;
    }

    public static final String OPEN_INTERVAL_UP_TIME = "open_interval_up_time";
    protected static Gson gson = new Gson();


    /**
     * 设置是否开启锁屏
     *
     * @param isOpen 是否开启锁屏
     */
    public void setOpenLockerAd(boolean isOpen) {
        AppOutBean appOutBean = getAppDialogBean();
        appOutBean.setOpenLock(isOpen);
        saveAppDialogBean(appOutBean);
    }

    /**
     * @return true:开启锁屏广告
     */
    public boolean isOpenLockerAd() {
        return getAppDialogBean().isOpenLock();
    }

    /**
     * 设置wifi切换
     *
     * @param isOpen true:开启wifi切换广告
     */
    public void setOpenWifiAd(boolean isOpen) {
        AppOutBean appOutBean = getAppDialogBean();
        appOutBean.setOpenWifi(isOpen);
        saveAppDialogBean(appOutBean);
    }

    public boolean isOpenWifiAd() {
        return getAppDialogBean().isOpenWifi();
    }


    /**
     * @param isOpen true:开启充电广告
     */
    public void setOpenRechargeAd(boolean isOpen) {
        AppOutBean appOutBean = getAppDialogBean();
        appOutBean.setOpenCharge(isOpen);
        saveAppDialogBean(appOutBean);
    }

    public boolean isOpenRechargeAd() {
        return getAppDialogBean().isOpenCharge();
    }

    /**
     * 监听到安装应用时弹出广告
     *
     * @param isOpen true:
     */
    public void setOpenInstallAd(boolean isOpen) {
        AppOutBean appOutBean = getAppDialogBean();
        appOutBean.setOpenInstall(isOpen);
        saveAppDialogBean(appOutBean);
    }

    public boolean isOpenInstallAd() {
        return getAppDialogBean().isOpenInstall();
    }


    /**
     * @param isOpen       true:开启应用外弹窗广告
     * @param intervalTime 时间间隔
     */
    public void setOpenIntervalAd(boolean isOpen, int intervalTime) {
        AppOutBean appOutBean = getAppDialogBean();
        appOutBean.setOpenOutDialog(isOpen);
        appOutBean.setOutDialogIntervalTime(intervalTime);
        saveAppDialogBean(appOutBean);
    }

    public void setIntervalUpTime() {
        mmkv.putLong(OPEN_INTERVAL_UP_TIME, System.currentTimeMillis()).commit();
    }

    /**
     * 已经开启应用外弹窗，切满足时间间隔
     * 同时满足开启状态、展示次数小于总次数，间隔时间大于intervalTime
     *
     * @return true:可以展示应用外弹窗
     */

    public boolean isOpenValidIntervalAd() {
        AppOutBean appOutBean = getAppDialogBean();
        return appOutBean.isOpenOutDialog()
                && appOutBean.getOutDialogCompleteTimes() < appOutBean.getOutDialogTimes()
                && showOutDialogIntervalTime() > appOutBean.getOutDialogIntervalTime();
    }

    public boolean isOpenIntervalAd() {
        return getAppDialogBean().isOpenOutDialog();
    }

    private long showOutDialogIntervalTime() {
        return System.currentTimeMillis() - mmkv.getLong(OPEN_INTERVAL_UP_TIME, 0);
    }

    /**
     * @return true:开启锁解锁播放视频
     */
    public boolean isOpenUnlockVideo() {
        AppOutBean bean = getAppDialogBean();
//        return bean.isUnLockVideo() && bean.getUnLockVideoCompleteTimes() < bean.getUnLockVideoTimes();
        Log.i("chyy", "isOpenUnLockVideo : " + getUpLockerPlayVideoTimes());
        return bean.isUnLockVideo() && getUpLockerPlayVideoTimes() < bean.getUnLockVideoTimes();
    }

    public void setUnlockVideo(boolean isOpen) {
        AppOutBean bean = getAppDialogBean();
        bean.setUnLockVideo(isOpen);
        saveAppDialogBean(bean);
    }


    public static final String APP_DIALOG_CONTROL = "app_dialog_control";

    public void saveAppDialogBean(AppOutBean bean) {
        mmkv.putString(APP_DIALOG_CONTROL, gson.toJson(bean)).commit();
    }

    public AppOutBean getAppDialogBean() {
        String appOutStr = mmkv.getString(APP_DIALOG_CONTROL, null);
        if (appOutStr != null) {
            return gson.fromJson(appOutStr, AppOutBean.class);
        }
        return new AppOutBean();
    }

    /**
     * 上一次锁屏启动时间
     */
    private static final String LOCKER_ACTIVITY_UP_TIME = "up_locker_time";

    public long getUpLockerActivityTime() {
        return mmkv.getLong(LOCKER_ACTIVITY_UP_TIME, 0);
    }

    public void setUpLockerTime() {
        mmkv.putLong(LOCKER_ACTIVITY_UP_TIME, System.currentTimeMillis()).commit();
    }

    /**
     * 解锁播放激励视频 次数
     */
    private static final String UN_LOCKER_PLAY_VIDEO_TIMES = "up_locker_play_video_times";

    public int getUpLockerPlayVideoTimes() {
        return mmkv.getInt(UN_LOCKER_PLAY_VIDEO_TIMES, 0);
    }

    public void setUnlockPlayVideoTimes() {
        long times = getUpLockerPlayVideoFirstTime();
        if (times == 0) {
            setUpLockerPlayVideoFirstTime();
        }
        long currentTimes = System.currentTimeMillis();

        if (!DateTimeUtils.isSameDay(times, currentTimes)) {
            mmkv.putInt(UN_LOCKER_PLAY_VIDEO_TIMES, 1).commit();
            setUpLockerPlayVideoFirstTime();
            return;
        }

        mmkv.putInt(UN_LOCKER_PLAY_VIDEO_TIMES, getUpLockerPlayVideoTimes() + 1).commit();
    }

    /**
     * 第一次播放激励视频时间
     */
    private static final String UN_LOCKER_PLAY_VIDEO_FIRST_TIME = "up_locker_play_video_first_time";

    public long getUpLockerPlayVideoFirstTime() {
        return mmkv.getLong(UN_LOCKER_PLAY_VIDEO_FIRST_TIME, 0);
    }

    public void setUpLockerPlayVideoFirstTime() {
        mmkv.putLong(UN_LOCKER_PLAY_VIDEO_FIRST_TIME, System.currentTimeMillis()).commit();
    }


}
