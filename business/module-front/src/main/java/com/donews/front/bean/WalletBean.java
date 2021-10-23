package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletBean extends BaseCustomViewModel {
    @SerializedName("list")
    private List<RpBean> list;

    public List<RpBean> getList() {
        return list;
    }

    public void setList(List<RpBean> list) {
        this.list = list;
    }

    public static class RpBean {
        @SerializedName("lottery_total")
        private int lotteryTotal;
        @SerializedName("opened")
        private Boolean opened;
        @SerializedName("had_lottery_total")
        private int hadLotteryTotal;

        public int getLotteryTotal() {
            return lotteryTotal;
        }

        public void setLotteryTotal(int lotteryTotal) {
            this.lotteryTotal = lotteryTotal;
        }

        public Boolean getOpened() {
            return opened;
        }

        public void setOpened(Boolean opened) {
            this.opened = opened;
        }

        public int getHadLotteryTotal() {
            return hadLotteryTotal;
        }

        public void setHadLotteryTotal(int hadLotteryTotal) {
            this.hadLotteryTotal = hadLotteryTotal;
        }
    }
}
