package com.donews.middle.utils;

import static com.donews.common.config.CritParameterConfig.CRIT_STATE;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.dn.sdk.bean.integral.ProxyIntegral;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;
import com.donews.common.bean.CritMessengerBean;
import com.donews.common.config.CritParameterConfig;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.application.MiddleModuleInit;
import com.donews.middle.bean.CriticalNumberBean;
import com.donews.middle.service.CritLotteryService;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.DateManager;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

public class CommonUtils {
    //开始暴击模式
    public static void startCrit() {
      //判断暴击模式是否处于开启中
        int critState = SPUtils.getInformain(CRIT_STATE, 0);
        if (critState == 0) {
            MiddleModuleInit.requestCriticalWallet(new MiddleModuleInit.ICriticalWalletListener() {
                @Override
                public void onError() {
                }
                @Override
                public void onSuccess(CriticalNumberBean numberBean) {
                    if (numberBean != null) {
                        //同步本地状态
                        DateManager.getInstance().putRefreshCritical(numberBean.getTotalTimes(), numberBean.getUseTimes());
                        //同步本地状态
                        if (SPUtils.getInformain(CRIT_STATE, 0) == 0) {
                            SPUtils.setInformain(CritParameterConfig.CRIT_START_TIME, SystemClock.elapsedRealtime());
                        }
                        SPUtils.setInformain(CRIT_STATE, 1);
                        //判断开启了多少次
                        //通知开始暴击模式  模拟开启暴击模式
                        EventBus.getDefault().post(new CritMessengerBean(200));
                        AnalysisUtils.onEventEx(BaseApplication.getInstance(), Dot.CRITICAL_NUMBER_AND_SUM_NUMBER,numberBean.getUseTimes()+"/"+numberBean.getTotalTimes()+"");


                    }
                }
            },"true");
        }
    }


    /**
     * 开启暴击体验监听服务
     *
     * @param context 需要的上下文
     */
    public static void startCritService(Context context, ProxyIntegral integralBean) {
        if (DateManager.getInstance().isAllowCritical()) {
            if (integralBean != null) {
                //开启暴击校验  (开始服务)
                Intent intent = new Intent(context, CritLotteryService.class);
                intent.putExtra("start_crit", true);
                intent.putExtra("start_time", ABSwitch.Ins().getScoreTaskPlayTime());
                intent.putExtra("wall_request_id", integralBean.getWallRequestId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(intent);
                }
            }

        }
    }

    public static boolean isAllRpOpened() {
        return SPUtils.getInformain(KeySharePreferences.OPENED_RED_PACKAGE_COUNTS, 0) >= 5;
    }
}
