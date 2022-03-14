package com.donews.base.base;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2021/1/6 10:43<br>
 * 版本：V1.0<br>
 */
public class AppStatusManager {
    public static AppStatusManager appStatusManager;
    public int appStatus = AppStatusConstant.STATUS_FORCE_KILLED; //APP状态 初始值为没启动 不在前台状态

    private AppStatusManager() {
    }

    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
