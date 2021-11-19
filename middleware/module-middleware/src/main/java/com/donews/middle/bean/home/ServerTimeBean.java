package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class ServerTimeBean extends BaseCustomViewModel {
    @SerializedName("now")
    private String now;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }
}
