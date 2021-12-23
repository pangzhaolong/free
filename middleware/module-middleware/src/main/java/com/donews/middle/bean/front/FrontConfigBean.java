package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FrontConfigBean extends BaseCustomViewModel {

    @SerializedName("banner")
    private Boolean banner;
    @SerializedName("bannerItems")
    private List<BannerItem> bannerItems;
    @SerializedName("lotteryWinner")
    private Boolean lotteryWinner;
    @SerializedName("redPackage")
    private Boolean redPackage;
    @SerializedName("refreshInterval")
    private int refreshInterval;
    @SerializedName("task")
    private Boolean task;
    @SerializedName("taskItems")
    private List<TaskItems> taskItems;

    public static class BannerItem {
        @SerializedName("action")
        private String action;
        @SerializedName("id")
        private int id;
        @SerializedName("img")
        private String img;
    }

    public static class TaskItems {
        @SerializedName("action")
        private String action;
        @SerializedName("icon")
        private String icon;
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
    }
}
