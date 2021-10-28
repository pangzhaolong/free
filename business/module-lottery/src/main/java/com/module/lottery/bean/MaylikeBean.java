package com.module.lottery.bean;


import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

//猜你喜欢源数据 May可能喜欢
public class MaylikeBean extends LotteryBean{


    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    @SerializedName("list")
    private List<ListDTO> list;

    public static class ListDTO  extends BaseCustomViewModel {
        @SerializedName("id")
        private String id;

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

        public Double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(Double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public Integer getLotteryStatus() {
            return lotteryStatus;
        }

        public void setLotteryStatus(Integer lotteryStatus) {
            this.lotteryStatus = lotteryStatus;
        }

        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("title")
        private String title;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("original_price")
        private Double originalPrice;
        @SerializedName("lottery_status")
        private Integer lotteryStatus;
    }
}
