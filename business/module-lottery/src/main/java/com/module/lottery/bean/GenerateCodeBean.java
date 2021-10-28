package com.module.lottery.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenerateCodeBean  extends BaseCustomViewModel {


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    @SerializedName("code")
    private String code;
    @SerializedName("remain")
    private Integer remain;
}
