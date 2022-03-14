package com.donews.common.bean;

import com.donews.common.contract.BaseCustomViewModel;

public class LotteryBackBean extends BaseCustomViewModel {

    float minProbability=0.884f;
    float maxProbability=0.990f;
    int backTimes=3;
    public int getBackTimes() {
        return backTimes;
    }

    public void setBackTimes(int backTimes) {
        this.backTimes = backTimes;
    }
    public float getMinProbability() {
        return minProbability;
    }

    public void setMinProbability(float minProbability) {
        this.minProbability = minProbability;
    }

    public float getMaxProbability() {
        return maxProbability;
    }

    public void setMaxProbability(float maxProbability) {
        this.maxProbability = maxProbability;
    }


}
