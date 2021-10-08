package com.dn.sdk;

import android.app.Application;
import android.content.Context;

import com.donews.base.utils.CrashHandlerUtil;
import com.donews.network.EasyHttp;

/**
 * @author by SnowDragon
 * Date on 2020/11/27
 * Description:
 */
public class TestApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化进程守护

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EasyHttp.init(this);

        CrashHandlerUtil.getInstance().init(this);

        AdLoadManager.getInstance().init(this, true);


    }
}
