package com.donews.middle.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class CriticalNumberBean extends BaseCustomViewModel {


    @SerializedName("open")
    private Boolean open;
    @SerializedName("total_times")
    private Integer totalTimes;
    @SerializedName("use_times")
    private Integer useTimes;
    @SerializedName("first")
    private Boolean first;
    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

}
