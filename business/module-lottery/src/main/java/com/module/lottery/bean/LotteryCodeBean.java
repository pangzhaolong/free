package com.module.lottery.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LotteryCodeBean implements Serializable {


    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    @SerializedName("codes")
    private List<String> codes;
}
