package com.donews.middle.bean.globle;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class CommandBean extends BaseCustomViewModel {
    @SerializedName("command") //单行口令
    private String command;
    @SerializedName("mulitCmd") //多行口令
    private String mulitCmd;
    @SerializedName("refreshInterval")
    private int refreshInterval = 60;

    public String getMulitCmd() {
        return mulitCmd;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public String getCommand() {
        return command;
    }


}
