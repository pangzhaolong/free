package com.donews.common.ad.business.utils

import com.donews.base.utils.ext.isToday
import com.donews.common.ad.business.bean.JddAdConfigBean
import com.donews.common.ad.business.monitor.LotteryAdCount
import com.donews.utilslibrary.utils.AppStatusUtils
import com.orhanobut.logger.Logger

/**
 *
 * 插屏工具类
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object InterstitialUtils {


    /** 判断是否打开了插屏广告 */
    fun checkOpenAd(bean: JddAdConfigBean): Boolean {
        //安装时间
        val installTime = AppStatusUtils.getAppInstallTime()
        val duration = bean.interstitialStartTime * 60 * 1000L
        if ((System.currentTimeMillis() - installTime) >= duration) {
            Logger.d("安装超过设置时间,开启插屏")
            return true
        }

        val totalLotteryNumber = LotteryAdCount.getTotalLotteryNumber()
        if (totalLotteryNumber >= bean.interstitialLotteryStartTime) {
            Logger.d("抽奖次数大于设置次数，开启插屏")
            return true
        }
        return false
    }
}