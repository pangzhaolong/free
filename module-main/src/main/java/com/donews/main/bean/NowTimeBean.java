package com.donews.main.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//封装服务器的时间
public class NowTimeBean implements Serializable {

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    @SerializedName("now")
    private String now;
}
