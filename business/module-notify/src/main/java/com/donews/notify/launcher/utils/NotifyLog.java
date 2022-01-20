package com.donews.notify.launcher.utils;

import android.util.Log;

import com.donews.base.BuildConfig;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;

/**
 * @author lcl
 * Date on 2022/1/20
 * Description:
 * 日志输出
 */
public class NotifyLog {
    public static void log(String msg){
        Log.e("notify",msg);
        if(BuildConfig.DEBUG){
            ToastUtil.showShort(BaseApplication.getInstance(),msg);
        }
    }
}
