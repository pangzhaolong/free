package com.module.lottery.utils

import com.donews.main.entitys.resps.ExitInterceptConfig
import com.donews.main.entitys.resps.NotLotteryConfig
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

class RandomProbability {
    companion object{
        var mExitInterceptConfig: ExitInterceptConfig? = null
        var mNotLotteryConfig: NotLotteryConfig? = null

        fun getRandomNumber(): String {
            if (mNotLotteryConfig == null) {
                if (mExitInterceptConfig == null) {
                    mExitInterceptConfig = ExitInterceptConfig()
                }
                mNotLotteryConfig = mExitInterceptConfig!!.notLotteryConfig
                val maxNumber = mNotLotteryConfig!!.maxProbability
                val minNumber = mNotLotteryConfig!!.minProbability
                var values=Random.nextDouble(maxNumber-minNumber)+minNumber;
                values *= 100
                val format = DecimalFormat("0.##")
                format.roundingMode = RoundingMode.FLOOR
                val probString = format.format(values)
                return probString;
            }
            return "";
        }
    }


}