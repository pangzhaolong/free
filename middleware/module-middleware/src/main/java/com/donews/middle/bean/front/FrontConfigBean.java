package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FrontConfigBean extends BaseCustomViewModel {

    @SerializedName("banner")
    private Boolean banner = false;
    @SerializedName("lotteryWinner")
    private Boolean lotteryWinner = true;
    @SerializedName("redPackage")
    private Boolean redPackage = true;
    @SerializedName("refreshInterval")
    private int refreshInterval = 60;
    @SerializedName("task")
    private Boolean task = false;
    @SerializedName("floating")
    private Boolean floating = false;
    @SerializedName("showTime")
    private Boolean showTime = false;
    @SerializedName("showTimeMsg")
    private Boolean showTimeMsg = false;
    @SerializedName("mine")
    private Boolean mine = false;
    @SerializedName("withDrawal")
    private Boolean withDrawal = false;
    @SerializedName("bannerItems")
    private SingleItem bannerItems;
    @SerializedName("taskItems")
    private List<YywItem> taskItems = new ArrayList<>();
    @SerializedName("floatingItems")
    private SingleItem floatingItems;
    @SerializedName("withDrawalItems")
    private SingleItem withDrawalItems;
    @SerializedName("frontTask")
    private TaskItem frontTask;
    @SerializedName("showTask")
    private TaskItem showTask;
    @SerializedName("showMsgTask")
    private TaskItem showMsgTask;
    @SerializedName("mineTask")
    private TaskItem mineTask;

    public Boolean getBanner() {
        return banner;
    }

    public SingleItem getBannerItems() {
        return bannerItems;
    }

    public Boolean getLotteryWinner() {
        return lotteryWinner;
    }

    public Boolean getRedPackage() {
        return redPackage;
    }

    public Boolean getTask() {
        return task;
    }

    public Boolean getShowTimeMsg() { return showTimeMsg; }

    public Boolean getShowTime() { return showTime; }

    public Boolean getFloating() { return floating; }

    public Boolean getWithDrawal() {
        return withDrawal;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public List<YywItem> getTaskItems() {
        return taskItems;
    }

    public SingleItem getFloatingItems() {
        return floatingItems;
    }

    public SingleItem getWithDrawalItems() {
        return withDrawalItems;
    }

    public Boolean getMine() {
        return mine;
    }

    public TaskItem getFrontTask() {
        return frontTask;
    }

    public TaskItem getShowTask() {
        return showTask;
    }

    public TaskItem getShowMsgTask() {
        return showMsgTask;
    }

    public TaskItem getMineTask() {
        return mineTask;
    }

    public static class YywItem extends BaseCustomViewModel {
        @SerializedName("action")
        private String action;
        @SerializedName("id")
        private int id;
        @SerializedName("img")
        private String img;
        @SerializedName("title")
        private String title;
        @SerializedName("model")
        private int model;

        public String getAction() {
            return action;
        }

        public int getId() {
            return id;
        }

        public String getImg() {
            return img;
        }

        public String getTitle() {
            return title;
        }

        public int getModel() {
            return model;
        }
    }

    public static class SingleItem extends BaseCustomViewModel {
        @SerializedName("switchInterval")
        private int switchInterval = 10;
        @SerializedName("items")
        List<YywItem> items = new ArrayList<>();

        public int getSwitchInterval() {
            return switchInterval;
        }

        public List<YywItem> getItems() {
            return items;
        }
    }

    public static class TaskItem extends BaseCustomViewModel {
        @SerializedName("switchInterval")
        private int switchInterval = 10;
        @SerializedName("taskGroup")
        private int taskGroup;
        @SerializedName("items")
        List<SubItems> items = new ArrayList<>();

        public int getSwitchInterval() {
            return switchInterval;
        }

        public int getTaskGroup() {
            return taskGroup;
        }

        public List<SubItems> getItems() {
            return items;
        }
    }

    public static class SubItems extends BaseCustomViewModel {
        @SerializedName("subItems")
        private List<YywItem> subItems = new ArrayList<>();

        public List<YywItem> getSubItems() {
            return subItems;
        }
    }
}
