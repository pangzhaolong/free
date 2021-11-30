package com.donews.alive.bean;


import com.donews.common.contract.BaseCustomViewModel;

/**
 * @author by SnowDragon
 * Date on 2020/12/9
 * Description:
 */
public class AppOutBean extends BaseCustomViewModel {
    /**
     * openCharge : true
     * openInstall : true
     * openLckerVideo : true
     * openLock : true
     * openLockVideoCompleteTimes : 1
     * openLockVideoTimes : 5
     * openOutDialog : true
     * openWifi : true
     * outDialogComplete : 2
     * outDialogIntervalTime : 1
     * outDialogTimes : 10
     */

    private boolean openCharge;
    private boolean openInstall;
    private boolean unLockVideo;
    private int unLockVideoCompleteTimes;
    private int unLockVideoTimes = 5;
    private boolean openLock;
    private boolean openWifi;
    private boolean openOutDialog;
    private int outDialogCompleteTimes;
    private int outDialogIntervalTime;
    private int outDialogTimes;

    public boolean isOpenCharge() {
        return openCharge;
    }

    public void setOpenCharge(boolean openCharge) {
        this.openCharge = openCharge;
    }


    public boolean isOpenInstall() {
        return openInstall;
    }

    public void setOpenInstall(boolean openInstall) {
        this.openInstall = openInstall;
    }


    public boolean isUnLockVideo() {
        return unLockVideo;
    }

    public void setUnLockVideo(boolean unLockVideo) {
        this.unLockVideo = unLockVideo;
    }


    public boolean isOpenLock() {
        return openLock;
    }

    public void setOpenLock(boolean openLock) {
        this.openLock = openLock;
    }


    public int getUnLockVideoCompleteTimes() {
        return unLockVideoCompleteTimes;
    }

    public void setUnLockVideoCompleteTimes(int unLockVideoCompleteTimes) {
        this.unLockVideoCompleteTimes = unLockVideoCompleteTimes;
    }


    public int getUnLockVideoTimes() {
        if (unLockVideoTimes == 0) {
            return 5;
        }
        return unLockVideoTimes;
    }

    public void setUnLockVideoTimes(int unLockVideoTimes) {
        this.unLockVideoTimes = unLockVideoTimes;
    }


    public boolean isOpenOutDialog() {
        return openOutDialog;
    }

    public void setOpenOutDialog(boolean openOutDialog) {
        this.openOutDialog = openOutDialog;
    }


    public boolean isOpenWifi() {
        return openWifi;
    }

    public void setOpenWifi(boolean openWifi) {
        this.openWifi = openWifi;
    }


    public int getOutDialogCompleteTimes() {
        return outDialogCompleteTimes;
    }

    public void setOutDialogCompleteTimes(int outDialogCompleteTimes) {
        this.outDialogCompleteTimes = outDialogCompleteTimes;
    }


    public int getOutDialogIntervalTime() {
        if (outDialogIntervalTime == 0) {
            return 60 * 1000;
        }
        return outDialogIntervalTime;
    }

    public void setOutDialogIntervalTime(int outDialogIntervalTime) {
        this.outDialogIntervalTime = outDialogIntervalTime;
    }


    public int getOutDialogTimes() {
        if (outDialogTimes == 0) {
            return 10;
        }
        return outDialogTimes;
    }

    public void setOutDialogTimes(int outDialogTimes) {
        this.outDialogTimes = outDialogTimes;
    }
}
