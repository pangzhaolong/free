package com.donews.base.base;

import android.app.Application;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 16:39<br>
 * 版本：V1.0<br>
 */
public  class ContextHolder {
    private  Application mApplication;


    public  void setAppContext(Application appContext){
        mApplication = appContext;
    }

    public  Application getApplication() {
        return mApplication;
    }

    private static class SingleHolder
    {
        private static ContextHolder instance = new ContextHolder();
    }

    public static ContextHolder getInstance()
    {
        return ContextHolder.SingleHolder.instance;
    }
}
