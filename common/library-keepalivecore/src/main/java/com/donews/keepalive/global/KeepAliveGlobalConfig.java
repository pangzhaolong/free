package com.donews.keepalive.global;

import android.content.Context;

import com.donews.base.storage.MmkvHelper;
import com.donews.common.NotifyLuncherConfigManager;

/**
 * 保活全局配置项
 * @author Swei
 * @date 2021/4/15 20:40
 * @since v1.0
 */
public class KeepAliveGlobalConfig {
    public static final String TAG = "keepalive-global";

    private static final String KEY_KEEPALIVE_OPEN_FIRST = "key_keepalive_open_first";

    public static boolean isFirstOpen(Context context){
        if(!MmkvHelper.isInit()){
            MmkvHelper.init(context);
        }
        return MmkvHelper.getInstance().getMmkv().decodeBool(KEY_KEEPALIVE_OPEN_FIRST, true);
    }

    public static void recordOpen(Context context){
        if(!MmkvHelper.isInit()){
            MmkvHelper.init(context);
        }
        MmkvHelper.getInstance().getMmkv().encode(KEY_KEEPALIVE_OPEN_FIRST, false);
    }

    public static long getDelayOpenTime(){
        return NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().keepAliveFirstDelayOpen;
    }
    public static long getMyDelayOpenTime(){
        return NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().myKeepAliveFirstDelayOpen;
    }

}
