package com.donews.common.contract;

import com.google.gson.annotations.SerializedName;

public class PreRegisterBean extends BaseCustomViewModel{
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("register_time")
    private String registerTime;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getUserId() {
        return userId;
    }

    public String getRegisterTime() {
        return registerTime;
    }
}
