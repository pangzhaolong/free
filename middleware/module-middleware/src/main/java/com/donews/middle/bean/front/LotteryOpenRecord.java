package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;


/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 当前开奖期数
 */

public class LotteryOpenRecord extends BaseCustomViewModel {
    @SerializedName("period")
    public int period;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}


