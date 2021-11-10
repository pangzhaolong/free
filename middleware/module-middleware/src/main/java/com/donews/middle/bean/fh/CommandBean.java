package com.donews.middle.bean.fh;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class CommandBean extends BaseCustomViewModel {
    @SerializedName("command")
    private String command;
    @SerializedName("test")
    private int test;

    @Override
    public String toString() {
        return "CommandBean{" +
                "command='" + command + '\'' +
                ", test=" + test +
                '}';
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

}
