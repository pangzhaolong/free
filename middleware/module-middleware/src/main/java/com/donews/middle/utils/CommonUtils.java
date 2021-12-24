package com.donews.middle.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.donews.common.bean.CritMessengerBean;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.service.CritLotteryService;
import com.donews.utilslibrary.utils.DateManager;

import org.greenrobot.eventbus.EventBus;

public class CommonUtils {
    //开始暴击模式
    public static void startCrit() {
        if (DateManager.getInstance().timesLimit(DateManager.CRIT_KEY, DateManager.CRIT_NUMBER,
                ABSwitch.Ins().getEnableOpenCritModelCount())) {
            //判断开启了多少次
            //通知开始暴击模式  模拟开启暴击模式
            EventBus.getDefault().post(new CritMessengerBean(200));
        }
    }

    /**
     * 开启暴击体验监听服务
     * @param context   需要的上下文
     * @param critState 是否开启后台体验监听
     */
    public static void startCritService(Context context, boolean critState) {
        if (DateManager.getInstance().timesLimit(DateManager.CRIT_KEY, DateManager.CRIT_NUMBER,
                ABSwitch.Ins().getEnableOpenCritModelCount())) {
            //开启暴击校验  (开始服务)
            Intent intent = new Intent(context, CritLotteryService.class);
            intent.putExtra("start_crit", true);
            intent.putExtra("start_time", ABSwitch.Ins().getScoreTaskPlayTime());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }


}
