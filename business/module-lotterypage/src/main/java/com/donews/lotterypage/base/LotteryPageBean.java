package com.donews.lotterypage.base;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LotteryPageBean extends BaseCustomViewModel {


    @SerializedName("period")
    private Integer period;
    @SerializedName("list")
    private List<ListDTO> list;

    public static class ListDTO implements Serializable {
        @SerializedName("title")
        private String title;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("display_price")
        private Integer displayPrice;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("lottery_status")
        private Integer lotteryStatus;
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public Integer getDisplayPrice() {
            return displayPrice;
        }

        public void setDisplayPrice(Integer displayPrice) {
            this.displayPrice = displayPrice;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public Integer getLotteryStatus() {
            return lotteryStatus;
        }

        public void setLotteryStatus(Integer lotteryStatus) {
            this.lotteryStatus = lotteryStatus;
        }


    }
    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

}
