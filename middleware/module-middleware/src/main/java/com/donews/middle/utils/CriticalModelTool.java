package com.donews.middle.utils;

import android.app.Notification;

import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.common.config.CritParameterConfig;
import com.donews.common.contract.LoginHelp;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CriticalModelTool {


    //判断是否是新用户
    public static boolean isNewUser() {
//        return true;
        //设备时长24小时
        long duration = 24 * 60 * 60 * 1000L;
        //新手标识
        boolean mark = SPUtils.getInformain(CritParameterConfig.LOTTERY_MARK, true);
        //取出用户注册时间
        String createdAt = LoginHelp.getInstance().getUserInfoBean().getCreatedAt();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = dateFormat.parse(createdAt);
            long registrationTime = date.getTime();
            //当前系统时间
            long systemTime = System.currentTimeMillis();
            //还需要判断 新手福利是否使用  mark
            if ((systemTime - registrationTime) <= duration && mark) {
                //是新用户
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //判断抽奖次数和最低抽奖次数一样
    public static boolean ifCoincide() {
        if (ABSwitch.Ins().getOpenCritModel()) {
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
                    return true;
                }
            }
        }
        return false;
    }


    //场景切换
    public static int getScenesSwitch() {
        //新用户并且抽奖次数未达到开启暴击条件
        //新用户的次数
        int sumNumber = ABSwitch.Ins().getOpenCritModelByLotteryCount();
        //已经参与的次数
        int participateNumber = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
        if (CriticalModelTool.isNewUser() && sumNumber < participateNumber) {
            //继续中抽奖次数逻辑
            return 1;
        } else {
            //判断下载，积分模式是否开启
            if (ABSwitch.Ins().getScoreModelCritCount() > 0) {
                //积分模式开启
                return 2;
            } else {
                return 1;
            }
        }


    }


    /**
     * 当前用户模式下。达到暴击模式需要的总次数
     *
     * @return
     */
    public static int getCurrentUserModulCount() {
        //新用户
        int sumNumber = 0;
        if (isNewUser()) {
            //总共需要抽多少个抽奖码开始暴击模式
            sumNumber = ABSwitch.Ins().getOpenCritModelByLotteryCount();
        } else {
            sumNumber = 6;
        }
        return sumNumber;
    }

    /**
     * 是否处于暴击时刻中
     */
    public static boolean ifCriticalStrike() {

        if (!AppInfo.checkIsWXLogin()) {
            return false;
        }

        if (ABSwitch.Ins().getOpenCritModel()) {
            //判断是否是暴击时刻
            int state = SPUtils.getInformain(CritParameterConfig.CRIT_STATE, 0);
            return state == 1;
        }
        return false;
    }


}
