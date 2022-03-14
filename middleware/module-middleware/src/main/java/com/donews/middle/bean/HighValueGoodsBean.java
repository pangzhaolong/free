package com.donews.middle.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HighValueGoodsBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<GoodsInfo> list;

    public List<GoodsInfo> getList() {
        return list;
    }

    public void setList(List<GoodsInfo> list) {
        this.list = list;
    }

    public static class GoodsInfo extends BaseCustomViewModel{
        @SerializedName("title")
        private String title;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("original_price")
        private Integer originalPrice;
        @SerializedName("display_price")
        private Integer displayPrice;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("total_people")
        private Integer totalPeople;
        @SerializedName("lucky_people")
        private Integer luckyPeople;
        @SerializedName("lottery_status")
        private Integer lotteryStatus;
        @SerializedName("src")
        private Integer src;
        @SerializedName("id")
        private String id;
        @SerializedName("category_name")
        private String categoryName;

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

        public Integer getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(Integer originalPrice) {
            this.originalPrice = originalPrice;
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

        public Integer getTotalPeople() {
            return totalPeople;
        }

        public void setTotalPeople(Integer totalPeople) {
            this.totalPeople = totalPeople;
        }

        public Integer getLuckyPeople() {
            return luckyPeople;
        }

        public void setLuckyPeople(Integer luckyPeople) {
            this.luckyPeople = luckyPeople;
        }

        public Integer getLotteryStatus() {
            return lotteryStatus;
        }

        public void setLotteryStatus(Integer lotteryStatus) {
            this.lotteryStatus = lotteryStatus;
        }

        public Integer getSrc() {
            return src;
        }

        public void setSrc(Integer src) {
            this.src = src;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
