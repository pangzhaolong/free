package com.module.lottery.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

//抽奖次数达到上限后,推荐一个抽奖商品
public class RecommendBean extends BaseCustomViewModel {
    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    @SerializedName("list")
        private List<ListDTO> list;

        public static class ListDTO implements Serializable {
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

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public Integer getDisplayPrice() {
                return displayPrice;
            }

            public void setDisplayPrice(Integer displayPrice) {
                this.displayPrice = displayPrice;
            }

            @SerializedName("title")
            private String title;
            @SerializedName("main_pic")
            private String mainPic;
            @SerializedName("goods_id")
            private String goodsId;
            @SerializedName("display_price")
            private Integer displayPrice;
        }
}
