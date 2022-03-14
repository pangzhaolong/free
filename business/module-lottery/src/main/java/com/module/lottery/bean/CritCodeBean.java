package com.module.lottery.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CritCodeBean  extends BaseCustomViewModel {


    public List<String> getCode() {
        return codes;
    }

    public void setCode(List<String> code) {
        this.codes = code;
    }

    @SerializedName("codes")
    private List<String> codes;
}
