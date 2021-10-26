package com.module.lottery.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenerateCodeBean implements Serializable {


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
