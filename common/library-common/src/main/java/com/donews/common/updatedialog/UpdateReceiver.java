package com.donews.common.updatedialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.donews.utilslibrary.utils.SPUtils;
import com.orhanobut.logger.Logger;

/**
 * @author by SnowDragon
 * Date on 2021/3/23
 * Description: 广播监听时间改变，定时检查版本更新
 */
public class UpdateReceiver extends BroadcastReceiver {
    private static final String CHECK_UPDATE_UP_TIME = "check_update_up_time";
    private static final long CHECK_INTERVAL_TIME = 5 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                if (SPUtils.getLongInformain(CHECK_UPDATE_UP_TIME, 0) == 0) {
                    SPUtils.setInformain(CHECK_UPDATE_UP_TIME, System.currentTimeMillis());
                    return;
                }
                long currentTime = System.currentTimeMillis();
                long lastUpdateTime = SPUtils.getLongInformain(CHECK_UPDATE_UP_TIME, 0);
                //每五分钟 进行一次检查更新
                if (currentTime - lastUpdateTime > CHECK_INTERVAL_TIME) {
                    SPUtils.setInformain(CHECK_UPDATE_UP_TIME, currentTime);
                    UpdateManager.getInstance().checkUpdate(context, false);
                }
            }
        }

    }
}
