package com.donews.middle.utils;

import com.donews.common.bean.CritMessengerBean;
import com.donews.middle.abswitch.ABSwitch;
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
}
