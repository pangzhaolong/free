package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NorGoodsBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<GoodsInfo> list;

    public List<GoodsInfo> getList() {
        return list;
    }

    public void setList(List<GoodsInfo> list) {
        this.list = list;
    }

    public static class GoodsInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("title")
        private String title;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("original_price")
        private Integer originalPrice;
        @SerializedName("lottery_status")
        private Integer lotteryStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

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

        public Integer getLotteryStatus() {
            return lotteryStatus;
        }

        public void setLotteryStatus(Integer lotteryStatus) {
            this.lotteryStatus = lotteryStatus;
        }
    }
}
