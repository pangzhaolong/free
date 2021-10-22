package com.donews.mine.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class CSBean extends BaseCustomViewModel {

    @SerializedName("city")
    private String city;
    @SerializedName("speed")
    private String speed;

    public CSBean(String city, String speed) {
        this.city = city;
        this.speed = speed;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
