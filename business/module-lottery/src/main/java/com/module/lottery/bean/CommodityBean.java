package com.module.lottery.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 抽奖页面，顶部的商品信息
 */
public class CommodityBean extends BaseCustomViewModel {


    @SerializedName("id")
    private String id;
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("title")
    private String title;
    @SerializedName("main_pic")
    private String mainPic;
    @SerializedName("original_price")
    private Double originalPrice;
    @SerializedName("display_price")
    private Integer displayPrice;
    @SerializedName("period")
    private Integer period;
    @SerializedName("item_link")
    private String itemLink;
    private ParticipateBean participateBean;
    private LotteryCodeBean lotteryCodeBean;

    private List<MaylikeBean.ListDTO> guessLikeData;
    private List<String> pics;
    public List<String> getPics() {
        return pics;
    }
    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public LotteryCodeBean getLotteryCodeBean() {
        return lotteryCodeBean;
    }

    public void setLotteryCodeBean(LotteryCodeBean lotteryCodeBean) {
        this.lotteryCodeBean = lotteryCodeBean;
    }

    public List<MaylikeBean.ListDTO> getGuessLikeData() {
        return guessLikeData;
    }

    public void setGuessLikeData(List<MaylikeBean.ListDTO> guessLikeData) {
        this.guessLikeData = guessLikeData;
    }

    public ParticipateBean getParticipateBean() {
        return participateBean;
    }

    public void setParticipateBean(ParticipateBean participateBean) {
        this.participateBean = participateBean;
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

    public Integer getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(Integer displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }


}
