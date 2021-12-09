package com.donews.middle.bean

class RedEnvelopeUnlockBean internal constructor(status: Int) {

    /**
     * 抽奖码解锁状态
     * 200解锁成功
     * 非200失败
     */
    var mStatus: Int = 0

    init {
        mStatus = status
    }

}


