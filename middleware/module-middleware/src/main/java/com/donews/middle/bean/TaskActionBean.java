package com.donews.middle.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class TaskActionBean extends BaseCustomViewModel {
    //###------------------------开奖页：action:{"page":"winner","ext":""}----------------###
    //###------------------------晒单页：action:{"page":"show","ext":""}------------------###
    //###------------------------抽奖页：action:{"page":"lottery","ext":"tb:12313"}-------###
    //###------------------------积分墙：action:{"page":"integral","ext":""}--------------###
    //###------------------------提现页：action:{"page":"money","ext":""}-----------------###

    public static final String WINNER = "winner";
    public static final String SHOW = "show";
    public static final String LOTTERY = "lottery";
    public static final String INTEGRAL = "integral";
    public static final String MONEY = "money";


    @SerializedName("page")
    private String page;
    @SerializedName("ext")
    private String ext;

    public TaskActionBean(String page, String ext) {
        this.page = page;
        this.ext = ext;
    }

    public String getPage() {
        return page;
    }

    public String getExt() {
        return ext;
    }
}