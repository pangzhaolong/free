package com.module.lottery.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖页面，顶部的商品信息
 */
public class CommodityBean extends BaseCustomViewModel {

    private List<WinLotteryBean.ListDTO> mParticipateList = new ArrayList<>();



    private LotteryCodeBean lotteryCodeBean;
    @SerializedName("title")
    private String title;
    @SerializedName("main_pic")
    private String mainPic;
    @SerializedName("display_price")
    private Integer displayPrice;
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("period")
    private Integer period;
    @SerializedName("pics")
    private List<String> pics;
    CommodityBean commodityBean;

    public CommodityBean getCommodityBean() {
        return commodityBean;
    }
    public List<WinLotteryBean.ListDTO> getParticipateList() {
        return mParticipateList;
    }
    public List<WinLotteryBean.ListDTO> getmParticipateList() {
        return mParticipateList;
    }

    public void setmParticipateList(List<WinLotteryBean.ListDTO> mParticipateList) {
        this.mParticipateList = mParticipateList;
    }

    public LotteryCodeBean getLotteryCodeBean() {
        return lotteryCodeBean;
    }

    public void setLotteryCodeBean(LotteryCodeBean lotteryCodeBean) {
        this.lotteryCodeBean = lotteryCodeBean;
    }
    public void setParticipateList(List<WinLotteryBean.ListDTO> mParticipateList) {
        this.mParticipateList = mParticipateList;
    }
    public void setCommodityBean(CommodityBean commodityBean) {
        this.commodityBean = commodityBean;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
