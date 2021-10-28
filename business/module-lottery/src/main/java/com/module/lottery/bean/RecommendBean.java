package com.module.lottery.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//抽奖次数达到上限后,推荐一个抽奖商品
public class RecommendBean  extends BaseCustomViewModel {


    @SerializedName("title")
    private String title;

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

    public Integer getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(Integer displayPrice) {
        this.displayPrice = displayPrice;
    }

    @SerializedName("main_pic")
    private String mainPic;
    @SerializedName("original_price")
    private Integer originalPrice;
    @SerializedName("id")
    private String id;
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("display_price")
    private Integer displayPrice;
}
