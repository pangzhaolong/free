package com.module.lottery.utils

import com.donews.common.appconfig.AppCommonConfigUtils
import com.donews.common.bean.AppCommonConfig
import com.donews.common.bean.LotteryBackBean
import com.donews.main.entitys.resps.ExitInterceptConfig
import com.donews.main.entitys.resps.NotLotteryConfig
import com.donews.main.utils.ExitInterceptUtils
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

class RandomProbability {
    companion object {
        var appCommonConfig: AppCommonConfig? = null
        var lotteryBackBean: LotteryBackBean? = null

        fun getRandomNumber(): String {
            if (lotteryBackBean == null) {
                if (appCommonConfig == null) {
                    appCommonConfig = AppCommonConfigUtils.getConfig();
                }
            }
            lotteryBackBean = appCommonConfig!!.lotteryBackBean;
            val maxNumber = lotteryBackBean!!.maxProbability
            val minNumber = lotteryBackBean!!.minProbability
            var values = Random.nextDouble(maxNumber.toDouble() - minNumber.toDouble()) + minNumber;
            values *= 100
            val format = DecimalFormat("0.##")
            format.roundingMode = RoundingMode.FLOOR
            val probString = format.format(values)
            return probString;
        }
    }


}