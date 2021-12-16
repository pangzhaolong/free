package com.module.lottery.utils;

import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.common.config.CritParameterConfig;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.utils.AppStatusUtils;
import com.donews.utilslibrary.utils.SPUtils;

public class CommonlyTool {


    //判断是否是新用户
    public static boolean isNewUser() {
        //判断是否是新用户
        //安装时间
        long installTime = AppStatusUtils.getAppInstallTime();
        //经过时间
        long elapsedTime = (System.currentTimeMillis() - installTime);
        //设备时长24小时
        long duration = 24 * 60 * 60 * 1000L;
        //新手标识
        boolean mark = SPUtils.getInformain(CritParameterConfig.LOTTERY_MARK, true);
        //还需要判断 新手福利是否使用  mark
        if (elapsedTime <= duration && mark) {
            //是新用户
            return true;
        }
        return false;

    }


    /**
     * 判断是否需要开启暴击模式
     */
    public static boolean ifTurnOnCrit() {
        if (ABSwitch.Ins().getOpenCritModel()) {
            //新用户
            int sumNumber = 0;
            //已经参与的次数
            int participateNumber = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
            if (isNewUser()) {
                //判断次数是否满足最低
                //总共需要抽多少个抽奖码开始暴击模式
                sumNumber = ABSwitch.Ins().getOpenCritModelByLotteryCount();
            } else {
                sumNumber = 6;
            }
            if (participateNumber >= sumNumber) {
                //开始暴击模式
                return true;
            }
        }
        return false;
    }


    //是否满足暴击模式
    public static boolean ifCriticalStrike() {
        if (ABSwitch.Ins().getOpenCritModel()) {
            //判断是否是暴击时刻
            int state = SPUtils.getInformain(CritParameterConfig.CRIT_STATE, 0);
            if (state == 1) {
                int sumNumber = 3;
                //新用户
                if (isNewUser()) {
                    //总共需要抽多少个抽奖码开始暴击模式
                    sumNumber = ABSwitch.Ins().getOpenCritModelByLotteryCount();
                } else {
                    sumNumber = 6;
                }
                //已经参与的次数
                int participateNumber = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
                if (participateNumber >= sumNumber) {
                    //满足
                    return true;
                } else {
                    //不满足
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }


}
