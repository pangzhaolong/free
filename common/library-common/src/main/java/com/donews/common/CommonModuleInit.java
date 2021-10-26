package com.donews.common;


import android.content.Intent;
import android.content.IntentFilter;

import com.donews.base.base.BaseApplication;
import com.donews.common.lifecycle.SimpleApplicationObServer;
import com.donews.common.updatedialog.UpdateReceiver;
import com.tencent.mmkv.MMKV;

/**
 * 应用模块:
 * <p>
 * 类描述: 通用库 & 基础库 自身初始化操作
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期： 2020-02-25
 */
public class CommonModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(BaseApplication application) {

        MMKV.initialize(application);
        //定时检查更新
        UpdateReceiver updateReceiver = new UpdateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        application.registerReceiver(updateReceiver, intentFilter);

        //更新app维度数据
//        AppGlobalConfigManager.update();

        new SimpleApplicationObServer().register();
        return false;
    }

    @Override
    public boolean onInitLow(BaseApplication application) {
        return false;
    }

}
