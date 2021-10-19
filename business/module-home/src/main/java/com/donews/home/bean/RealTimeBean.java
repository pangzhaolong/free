package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RealTimeBean extends BaseCustomViewModel {


    @SerializedName("list")
    private List<goodsInfo> list;

    public List<goodsInfo> getList() {
        return list;
    }

    public void setList(List<goodsInfo> list) {
        this.list = list;
    }

    public static class goodsInfo extends BaseCustomViewModel{
        @SerializedName("id")
        private String id;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("ranking")
        private Integer ranking;
        @SerializedName("new_ranking_goods")
        private Integer newRankingGoods;
        @SerializedName("dtitle")
        private String dtitle;
        @SerializedName("actual_price")
        private float actualPrice;
        @SerializedName("coupon_price")
        private float couponPrice;
        @SerializedName("month_sales")
        private Integer monthSales;
        @SerializedName("two_hours_sales")
        private Integer twoHoursSales;
        @SerializedName("daily_sales")
        private Integer dailySales;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("title")
        private String title;
        @SerializedName("original_price")
        private float originalPrice;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("activity_type")
        private Integer activityType;
        @SerializedName("shop_type")
        private Integer shopType;
        @SerializedName("seller_id")
        private String sellerId;
        @SerializedName("quan_m_link")
        private Integer quanMLink;
        @SerializedName("hz_quan_over")
        private Integer hzQuanOver;
        @SerializedName("yunfeixian")
        private Integer yunfeixian;
        @SerializedName("freeship_remote_district")
        private Integer freeshipRemoteDistrict;
        @SerializedName("fresh")
        private Integer fresh;
        @SerializedName("lowest")
        private Integer lowest;

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

        public Integer getRanking() {
            return ranking;
        }

        public void setRanking(Integer ranking) {
            this.ranking = ranking;
        }

        public Integer getNewRankingGoods() {
            return newRankingGoods;
        }

        public void setNewRankingGoods(Integer newRankingGoods) {
            this.newRankingGoods = newRankingGoods;
        }

        public String getDtitle() {
            return dtitle;
        }

        public void setDtitle(String dtitle) {
            this.dtitle = dtitle;
        }

        public float getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(float actualPrice) {
            this.actualPrice = actualPrice;
        }

        public float getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(float couponPrice) {
            this.couponPrice = couponPrice;
        }

        public Integer getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(Integer monthSales) {
            this.monthSales = monthSales;
        }

        public Integer getTwoHoursSales() {
            return twoHoursSales;
        }

        public void setTwoHoursSales(Integer twoHoursSales) {
            this.twoHoursSales = twoHoursSales;
        }

        public Integer getDailySales() {
            return dailySales;
        }

        public void setDailySales(Integer dailySales) {
            this.dailySales = dailySales;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public float getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(float originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Integer getActivityType() {
            return activityType;
        }

        public void setActivityType(Integer activityType) {
            this.activityType = activityType;
        }

        public Integer getShopType() {
            return shopType;
        }

        public void setShopType(Integer shopType) {
            this.shopType = shopType;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public Integer getQuanMLink() {
            return quanMLink;
        }

        public void setQuanMLink(Integer quanMLink) {
            this.quanMLink = quanMLink;
        }

        public Integer getHzQuanOver() {
            return hzQuanOver;
        }

        public void setHzQuanOver(Integer hzQuanOver) {
            this.hzQuanOver = hzQuanOver;
        }

        public Integer getYunfeixian() {
            return yunfeixian;
        }

        public void setYunfeixian(Integer yunfeixian) {
            this.yunfeixian = yunfeixian;
        }

        public Integer getFreeshipRemoteDistrict() {
            return freeshipRemoteDistrict;
        }

        public void setFreeshipRemoteDistrict(Integer freeshipRemoteDistrict) {
            this.freeshipRemoteDistrict = freeshipRemoteDistrict;
        }

        public Integer getFresh() {
            return fresh;
        }

        public void setFresh(Integer fresh) {
            this.fresh = fresh;
        }

        public Integer getLowest() {
            return lowest;
        }

        public void setLowest(Integer lowest) {
            this.lowest = lowest;
        }
    }
}
