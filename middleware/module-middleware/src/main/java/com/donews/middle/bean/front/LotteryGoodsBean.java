package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LotteryGoodsBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<GoodsInfo> list;

    public List<GoodsInfo> getList() {
        return list;
    }

    public void setList(List<GoodsInfo> list) {
        this.list = list;
    }

    public static class GoodsInfo  extends BaseCustomViewModel{
        @SerializedName("id")
        private String id;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("title")
        private String title;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("original_price")
        private float originalPrice;
        @SerializedName("lottery_status")
        private int lotteryStatus;

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

        public float getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(float originalPrice) {
            this.originalPrice = originalPrice;
        }

        public int getLotteryStatus() {
            return lotteryStatus;
        }

        public void setLotteryStatus(int lotteryStatus) {
            this.lotteryStatus = lotteryStatus;
        }
    }
}
