package com.donews.middle.utils;

import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.common.config.CritParameterConfig;
import com.donews.common.contract.LoginHelp;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DateManager;
import com.donews.utilslibrary.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CriticalModelTool {


    //判断是否是新用户
    public static boolean isNewUser() {
        //是否开启新用户模式
        if (OtherSwitch.Ins().isOpenCritModelByNewUser()) {
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
        }
        return false;
    }


    //判断抽奖次数和最低抽奖次数一样(前提是处于普通次数模式)
    public static boolean ifCoincide() {
        //开启了暴击模式
        if (OtherSwitch.Ins().getOpenCritModel() && DateManager.getInstance().isAllowCritical()) {
            //新用户
            int sumNumber = 0;
            //已经参与的次数
            int participateNumber = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
            if (isNewUser()) {
                //判断次数是否满足最低
                //总共需要抽多少个抽奖码开始暴击模式
                sumNumber = OtherSwitch.Ins().getOpenCritModelByNewUserCount();
            } else {
                sumNumber = OtherSwitch.Ins().getOpenCritModelByOldUserCount();
            }

            if (participateNumber >= sumNumber) {
                return true;
            }
        }
        return false;
    }

    public static boolean canShowCriticalBtn() {
        if (!AppInfo.checkIsWXLogin()) {
            return false;
        }
        if (!OtherSwitch.Ins().getOpenCritModel()) {
            return false;
        }
        if (!DateManager.getInstance().isAllowCritical()) {
            return false;
        }

        if (SPUtils.getInformain(CritParameterConfig.CRIT_STATE, 0) == 1) {
            return false;
        }
        
        return true;
    }


    public interface IScenesSwitchListener {
        void onIntegralNumber(ProxyIntegral integralBean);
    }


    //场景切换
    public static void getScenesSwitch(final IScenesSwitchListener iScenesSwitchListener) {
        if (OtherSwitch.Ins().getOpenCritModel()) {
            if (iScenesSwitchListener != null) {
                //新用户并且抽奖次数未达到开启暴击条件
                //新用户的次数
                int sumNumber = OtherSwitch.Ins().getOpenCritModelByNewUserCount();
                //已经参与的次数
                int participateNumber = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
                boolean markTEststst = SPUtils.getInformain(CritParameterConfig.LOTTERY_MARK, true);
                if (CriticalModelTool.isNewUser() && participateNumber <= sumNumber && markTEststst) {
                    //继续中抽奖次数逻辑(新手进行中)
                    iScenesSwitchListener.onIntegralNumber(null);
                    return;
                }
                //非新手
                if (OtherSwitch.Ins().isOpenScoreModelCrit()) {
                    IntegralComponent.getInstance().getIntegral(new IntegralComponent.IntegralHttpCallBack() {
                        @Override
                        public void onSuccess(ProxyIntegral integralBean) {
                            iScenesSwitchListener.onIntegralNumber(integralBean);
                        }

                        @Override
                        public void onError(String var1) {
                            iScenesSwitchListener.onIntegralNumber(null);
                        }

                        @Override
                        public void onNoTask() {
                            iScenesSwitchListener.onIntegralNumber(null);
                        }

                    });
                } else {
                    //没有开启下载模式
                    iScenesSwitchListener.onIntegralNumber(null);
                }


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
            sumNumber = OtherSwitch.Ins().getOpenCritModelByNewUserCount();
        } else {
            sumNumber = OtherSwitch.Ins().getOpenCritModelByOldUserCount();
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

        if (OtherSwitch.Ins().getOpenCritModel()) {
            //判断是否是暴击时刻
            int state = SPUtils.getInformain(CritParameterConfig.CRIT_STATE, 0);
            return state == 1;
        }
        return false;
    }
}
