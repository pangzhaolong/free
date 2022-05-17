package com.donews.turntable.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class RewardedBean extends BaseCustomViewModel {


    @SerializedName("id")
    private Integer id;
    @SerializedName("coin")
    private Integer coin;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

}
