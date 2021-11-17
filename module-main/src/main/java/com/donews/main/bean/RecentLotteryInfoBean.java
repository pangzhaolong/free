package com.donews.main.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class RecentLotteryInfoBean extends BaseCustomViewModel {

    @SerializedName("joined")
    private Boolean joined;
    @SerializedName("period")
    private Integer period;
    @SerializedName("now")
    private String now;

    public Boolean getJoined() {
        return joined;
    }

    public void setJoined(Boolean joined) {
        this.joined = joined;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

}
